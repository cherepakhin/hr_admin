#!/bin/bash
for file in ./*.webp
do
if [ -f "$file" ]
then
echo "$file is a file"
dwebp "$file" -o "$(basename "$file" .webp).png"
# mv "$file" "$(basename "$file" .html).txt"
#dwebp "$file" "$(basename.png)"

fi
done