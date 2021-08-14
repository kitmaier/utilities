# convert midi file to list of note values and times as raw hex
# http://www.music.mcgill.ca/~ich/classes/mumt306/StandardMIDIfileformat.html
# https://stackoverflow.com/questions/3964245/convert-file-to-hex-string-python

filename = '/Users/8maie/OneDrive/Desktop/Easy Keys Midi Files/Hit The Road, Jack - Ray Charles (70 BPM).mid'
with open(filename,'rb') as f:
  content = f.read().hex()

# MThd (Header)
if content[0:8]=='4d546864':
  print("found content[0:8]=='4d546864'")
  content = content[8:]
else:
  print("expected content[0:8]=='4d546864'")
  exit()

# Format 0 - 1 track - 480 ticks
if content[0:20]=='000000060000000101e0':
  print("found content[0:20]=='000000060000000101e0'")
  content = content[20:]
else:
  print("expected content[0:20]=='000000060000000101e0'")
  exit()

# MTrk (Header)
if content[0:8]=='4d54726b':
  print("found content[0:8]=='4d54726b'")
  content = content[8:]
else:
  print("expected content[0:8]=='4d54726b'")
  exit()

# Data length - 2905 bytes
if content[0:8]=='00000b59':
  print("found content[0:8]=='00000b59'")
  content = content[8:]
else:
  print("expected content[0:8]=='00000b59'")
  exit()

while len(content)>0:
  # get time delta (variable length number field)
  delta = ""
  while len(delta)==0 or delta[-2] in '89abcdef':
    delta = delta+content[0:2]
    content = content[2:]
  # get first byte, condition on value, maybe get second byte to specify
  #   first check on first bit, if zero then skip straight to next step
  if content[0] in '89abcdef':
    if content[0:2] in ['90','b0']:
      command = content[0:2]
      content = content[2:]
    elif content[0:2] in ['ff']:
      command = content[0:2]
      content = content[4:]
      if content[0:1] in ['0']:
        fflen = int(content[1:2])
        content = content[2:]
        for k in range(fflen):
          content = content[2:]
      else: 
        print("ff len greater than f:"+content[0:2])
        break
    else:
      print("unknown command:"+content[0:2])
      break
  # get fixed number of bytes
  if command in ['90','b0']:
    data = content[0:4]
    content = content[4:]
  if command in ['90']:
    print(delta,"|",command,"|",data)
