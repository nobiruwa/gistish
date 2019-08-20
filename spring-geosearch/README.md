# spring-geosearch

## GET /location/{id}

```
http://localhost:8080/location/1
{"id":1,"prefectureName":"東京都","cityName":"千代田区","townName":"内幸町二丁目","geometric":{"lon":139.754182,"lat":35.670812}}
```

## GET /location

```
http://localhost:8080/location?prefecture=%E6%9D%B1%E4%BA%AC%E9%83%BD
[{"id":1,"prefectureName":"東京都","cityName":"千代田区","townName":"内幸町二丁目","geometric":{"lon":139.754182,"lat":35.670812}}, ... ,{"id":5358,"prefectureName":"東京都","cityName":"小笠原村","townName":"西之島","geometric":{"lon":140.876291,"lat":27.247}}]
```

```
http://localhost:8080/location?prefecture=%E6%9D%B1%E4%BA%AC%E9%83%BD&city=%E5%8C%97%E5%8C%BA
[{"id":2017,"prefectureName":"東京都","cityName":"北区","townName":"堀船二丁目","geometric":{"lon":139.745645,"lat":35.754421}}, ... ,{"id":2129,"prefectureName":"東京都","cityName":"北区","townName":"田端五丁目","geometric":{"lon":139.757109,"lat":35.737828}}]
```

```
http://localhost:8080/location?prefecture=%E6%9D%B1%E4%BA%AC%E9%83%BD&city=%E5%8C%97%E5%8C%BA&town=%E8%B1%8A%E5%B3%B6
[{"id":2026,"prefectureName":"東京都","cityName":"北区","townName":"豊島三丁目","geometric":{"lon":139.745526,"lat":35.760896}}, ... ,{"id":2068,"prefectureName":"東京都","cityName":"北区","townName":"豊島一丁目","geometric":{"lon":139.740447,"lat":35.75767}}]
```

## GET /distance

```
http://localhost:8080/distance?lon=139.745526&lat=35.760896&within=1000
[{"id":2026,"prefectureName":"東京都","cityName":"北区","townName":"豊島三丁目","geometric":{"lon":139.745526,"lat":35.760896}},{"id":2027,"prefectureName":"東京都","cityName":"北区","townName":"豊島二丁目","geometric":{"lon":139.744286,"lat":35.758124}},{"id":2030,"prefectureName":"東京都","cityName":"北区","townName":"豊島六丁目","geometric":{"lon":139.746497,"lat":35.765171}},{"id":2031,"prefectureName":"東京都","cityName":"北区","townName":"豊島四丁目","geometric":{"lon":139.749883,"lat":35.762118}},{"id":2060,"prefectureName":"東京都","cityName":"北区","townName":"豊島五丁目","geometric":{"lon":139.754694,"lat":35.76628}},{"id":2064,"prefectureName":"東京都","cityName":"北区","townName":"豊島七丁目","geometric":{"lon":139.743873,"lat":35.764115}},{"id":2065,"prefectureName":"東京都","cityName":"北区","townName":"豊島八丁目","geometric":{"lon":139.740187,"lat":35.766025}},{"id":2068,"prefectureName":"東京都","cityName":"北区","townName":"豊島一丁目","geometric":{"lon":139.740447,"lat":35.75767}}]
```
