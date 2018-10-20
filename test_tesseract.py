import pytesseract
import cv2
import re
import regex
from PIL import Image, ImageEnhance, ImageFilter

# img_path = "testreceipt1.png"
# out_dir = ""
# img = cv2.imread(img_path)
# for x in range(0, 14):
#     numindex = str(x)
#     configstring = '--psm %d' % x
#     text = pytesseract.image_to_string(img, lang='eng', config=configstring)
#     out_file = re.sub(".png","_v" + numindex + ".txt",img_path.split("\\")[-1])
#     out_path = out_dir + out_file
#     fd = open(out_path,"w")
#     fd.write("%s" %text)
#     fd.close()


text = pytesseract.image_to_string(Image.open('testreceipt2.png'))
fd = open('testreceipt2.txt', 'w')
fd.write("%s" % text)
fd.close()

out_path = 'testreceipt2.txt'
fd = open(out_path,'r')
lines = fd.readlines()
num_list = []

for line in lines:
    r = regex.compile('.*(Total){e<=2}.*')
    list = r.match(line)
    if list is not None:
        try:
            nums = re.findall("\d+\.\d+", line)
            for num in nums:
                num_list.append(float(num))
        except:
            continue

print(num_list)
print(max(num_list))
print(type(max(num_list)))
