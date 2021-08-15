# convert midi file to list of note values and times as raw hex
# http://www.music.mcgill.ca/~ich/classes/mumt306/StandardMIDIfileformat.html
# https://stackoverflow.com/questions/3964245/convert-file-to-hex-string-python

#filename = '/Users/8maie/OneDrive/Desktop/Easy Keys Midi Files/Hit The Road, Jack - Ray Charles (70 BPM).mid'
filename = '/Users/8maie/OneDrive/Desktop/Easy Keys Midi Files/Hey, Soul Sister - Train (105 BPM).mid'
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

# Data length
print("data length: "+content[0:8])
content = content[8:]

timenum = 0
data = "UNK"
mintime = 100000000000000000
maxtime = 0
noteranges = []
for k in range(200):
  noteranges.append([])

while len(content)>0:
  # get time delta (variable length number field)
  delta = ""
  deltanum = 0
  while len(delta)==0 or delta[-2] in '89abcdef':
    deltabyte = content[0:2]
    content = content[2:]
    delta = delta+deltabyte
    deltanum = deltanum*128+(int(deltabyte,16)&127)
  timenum += deltanum
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
  if command=='90':
    #print(timenum,"|",int(data[0:2],16),"|",data[2:4])
    mintime = min(mintime,timenum)
    maxtime = max(maxtime,timenum)
    notenum = int(data[0:2],16)
    state = "STOP" if data[2:4]=='00' else "START"
    noteranges[notenum].append([timenum,state])

print(mintime,maxtime)
for x in range(200):
  print(x,noteranges[x])

endtick = ((maxtime-mintime)+60)//120
print(endtick)

noteticks = []
for k in range(200):
  noteticks.append([])
  if len(noteranges[k])==0:
    continue
  last = 0
  for event in noteranges[k]:
    next = ((event[0]-mintime)+60)//120
    for x in range(next-last):
      if event[1]=="START":
        noteticks[k].append(0)
      else:
        noteticks[k].append(1)
    last = next

for x in range(200):
  print(x,noteticks[x])

for tick in range(endtick):
  row = ""
  for x in range(200):
    if len(noteticks[x])-1<tick:
      row = row+"0;"
    else:
      row = row+str(noteticks[x][tick])+";"
  print(row)


