#!/usr/bin/env python3
# -*- coding:utf-8 -*-

from PIL import Image
import sys
import pyocr
import pyocr.builders
import cv2
import numpy as np

tools = pyocr.get_available_tools()

if len(tools) == 0:
    print("No OCR tool found")
    sys.exit(1)

tool = tools[0]

img = cv2.imread("resource/qiita-image.png", cv2.IMREAD_GRAYSCALE)

#座標の指定は x, y, width, Height
box_area = np.array([[18, 7, 792, 100],
                     [23, 128, 53, 27],
                     [90, 128, 65, 23],
                     [18, 221, 122, 43],
                     [18, 294, 751, 87]])

for box in box_area:
    #イメージは OpenCV -> PIL に変換する
    txt = tool.image_to_string(Image.fromarray(img[box[1]:box[1]+box[3], box[0]:box[0]+box[2]]), lang="jpn", builder=pyocr.builders.TextBuilder(tesseract_layout=6))

    print(txt)
