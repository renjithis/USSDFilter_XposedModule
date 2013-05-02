USSDFilter_XposedModule
=======================

USSD Filter using the Xposed Framework.<br />
Currently only 1 filter is supported using the file /sdcard(or equivalent)/USSDFilterString.conf<br />
Substring matching implemented with Toast notification.<br />
UI is not done yet<br />

TODO 
====

1. Preference activity<br />
  1.1 Add button to add a filter<br />
  1.2 Long press to show remove option<br />
  1.3 Press to edit<br />
2. Filter types :<br />
  2.0 Priorty - number from 1. default = lowest<br />
  2.1 Substring<br />
  2.2 RegEx<br />
3. Filter output:<br />
  3.1 No ouput - completely suppress message<br />
  3.2 Toast the message<br />
  3.3 Show as notification<br />
4. Donate (consumables for $1, $2 & $5)<br />
5. About<br />