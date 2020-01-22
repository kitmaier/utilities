* how to dump chrome tabs from android device
* based on tutorial at https://android.stackexchange.com/questions/56635/how-can-i-export-the-list-of-open-chrome-tabs/193858#193858
* open dev tools in desktop browser with F12
* pop out dev tools window with vertical ellipsis menu in the upper right
* open vertical ellipsis menu > More tools > Remote devices
* connect phone to laptop and allow remote debugging
* select phone in remote devices listing
* hit Ctrl+Shift+J to open editor on the editor
* run the below script to copy list of tabs to clipboard, then paste into a text editor

```tabs = Array.from(document.querySelector('div /deep/ div /deep/ div /deep/ div /deep/ div /deep/ div /deep/ div.vbox.flex-auto').shadowRoot.querySelectorAll('.devices-view .device-page-list .vbox'), s => ({name: s.querySelector('.device-page-title').textContent, url: s.querySelector('.device-page-url .devtools-link').getAttribute('href')}))
str = '';
for (i=0;i<tabs.length;i++){
  str += '['+tabs[i]['name']+']('+tabs[i]['url']+')\n'
}
copy(str)```