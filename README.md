SCTags is ctags and etags extractor for Scala language

Usage
-----

```
  sctags [-f|-o file] [-r|--recurse] [-v|--version] <files>

-f file | -o file
  sets the file the tags are written to, default is "tags"
  (Use "-" for standard output)

-r --recurse
  recurse through directories specified on the command line

-v --version
  print fake version for exhuberant ctags compatibility
```

Copyright
----------

This package is based on http://programmer-monk.net/darcs/repos/sctags

It is simplified and extended to support scope tracking.

Released under the Apache 2.0 License.

Copyright 2008 Geoff Reedy 

Copyright 2015 Luben Karavelov
