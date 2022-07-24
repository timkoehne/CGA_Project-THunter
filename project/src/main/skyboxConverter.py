from PIL import Image
import sys

image = Image.open(sys.argv[1])
filepath = sys.argv[1][:sys.argv[1].rindex("/")]

sides = ("", "top", "", "", "left", "front",
         "right", "back", "", "bottom", "", "")

new_width = image.width/4
new_height = image.height/3

for y in range(3):
    for x in range(4):
        if(sides[y*4+x] != ""):
            print(sides[y*4+x])
            tile = image.crop(
                (new_width * x, new_height * y, new_width * x + new_width, new_height * y + new_height))
            tile.save(filepath + "/" + sides[y*4+x]+".png")
