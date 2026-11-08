```text
Creating big.bin...
big.bin created
Copy without buffer
Copied 20 MB
Copied 40 MB
Copied 60 MB
Copied 80 MB
Copied 100 MB
Done without buffer: 965087 ms
Copy with buffer
Copied 20 MB
Copied 40 MB
Copied 60 MB
Copied 80 MB
Copied 100 MB
Done with buffer: 6551 ms

Without buffer: 965087 ms
Buffered: 6551 ms
Buffered 147 times faster
```

### Обоснование
Основное ускорение достигается за счёт уменьшения количества операций ввода-вывода и связанных с ними накладных расходов.
Без буфера программа читает и записывает данные по одному байту за раз.
Это приводит к большому количеству обращений к ОС, каждое из которых имеет дополнительные накладные расходы.
BufferedInputStream и BufferedOutputStream используют внутренние массивы (в нашем случае по 64 КБ), позволяя читать и записывать данные крупными батчами, а не по одному байту.
Благодаря этому количество обращений к ОС значительно сокращается.