import re
import sqlite3

from base64 import b64encode, b64decode
from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from Crypto.Util.Padding import pad, unpad

# バイト長16(文字列長32)の文字列
re_hex_16bytes = re.compile(r'^[0-9a-fA-F]{32}$')

db = None

def init_db():
    global db

    db = sqlite3.connect('key.db')

    cur = db.cursor()

    cur.execute('CREATE TABLE IF NOT EXISTS key(r1, r2, PRIMARY KEY (r1))')

def generate_r1(length_bytes=16):
    return get_random_bytes(length_bytes).hex()

def generate_r2(r1, length_bytes=16):
    assert(re_hex_16bytes.match(r1))

    r2 = get_random_bytes(length_bytes).hex()

    assert(re_hex_16bytes.match(r2))

    cur = db.cursor()

    cur.execute('INSERT INTO key(r1, r2) VALUES(?, ?) ON CONFLICT(r1) DO UPDATE SET r2=?', (r1, r2, r2))

    return r2

def encrypt(r1, r2, plain_text):
    cipher = AES.new(
        key=bytes.fromhex(r1),
        mode=AES.MODE_CBC,
        iv=bytes.fromhex(r2),
    )

    return b64encode(cipher.encrypt(pad(plain_text.encode('utf-8'), AES.block_size))).decode('utf-8')

def decrypt(r2, encrypted_text):
    cur = db.cursor()
    cur.execute('SELECT r1 FROM key WHERE r2 = ?', (r2,))
    (r1,) = cur.fetchone()

    cipher = AES.new(
        key=bytes.fromhex(r1),
        mode=AES.MODE_CBC,
        iv=bytes.fromhex(r2),
    )

    return unpad(cipher.decrypt(b64decode(encrypted_text)), AES.block_size).decode('utf-8')

def test(plain_text='ThisIsSecretP@ssword!!!'):
    try:
        init_db()

        # 送信側でr1 = Keyを生成する
        r1 = generate_r1()

        # 送信側はr2 = IVを得る
        # 受信側はr1とr2のペアを保存する
        r2 = generate_r2(r1)

        # 送信側でr1とr2を使ってAESによる暗号化を行う
        encrypted_text = encrypt(r1=r1, r2=r2, plain_text=plain_text)

        # 受信側でr1とr2を使ってAESによる復号を行う
        # 受信側はr2と暗号化されたデータを受信側に渡す
        decrypted_text = decrypt(r2=r2, encrypted_text=encrypted_text)

        # 受信側で復号された平文を扱う
        print(decrypted_text)

    finally:
        if db:
            db.close()


if __name__ == '__main__':
    test()
