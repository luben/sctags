SCTags will parse scala source code and write a ctags-compatible tags file.

= Usage =

Unix:
  sctags [-f|-o file] [-r|--recurse] [-v|--version] <files>

-f file | -o file
  sets the file the tags are written to, default is "tags"
  (Use "-" for standard output)

-r --recurse
  recurse through directories specified on the command line

-v --version
  print fake version for exhuberant ctags compatibility
