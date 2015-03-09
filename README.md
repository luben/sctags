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

Example Vim + TagBar config
---------------------------
```
function! SCTags()
    if executable("sctags")
        let g:tagbar_ctags_bin = "sctags"
        let g:tagbar_type_scala = {
            \ 'ctagstype' : 'scala',
            \ 'sro'       : '.',
            \ 'kinds'     : [
                \ 'p:packages',
                \ 'V:values',
                \ 'v:variables',
                \ 'T:types',
                \ 't:traits',
                \ 'o:objects',
                \ 'O:cobjects',
                \ 'c:classes',
                \ 'C:cclasses',
                \ 'm:methods:1'
            \ ],
            \ 'kind2scope'  : {
                \ 'p' : 'package',
                \ 'T' : 'type',
                \ 't' : 'trait',
                \ 'o' : 'object',
                \ 'O' : 'case_object',
                \ 'c' : 'class',
                \ 'C' : 'case_class',
                \ 'm' : 'method'
            \ },
            \ 'scope2kind'  : {
                \ 'package' : 'p',
                \ 'type' : 'T',
                \ 'trait' : 't',
                \ 'object' : 'o',
                \ 'case_object' : 'O',
                \ 'class' : 'c',
                \ 'case_class' : 'C',
                \ 'method' : 'm'
            \ }
        \ }
    endif
endfunction

if has("autocmd")
    autocmd FileType scala call SCTags()
endif
```

Copyright
----------

This package is based on http://programmer-monk.net/darcs/repos/sctags

It is simplified and extended to support scope tracking.

Released under the Apache 2.0 License.

Copyright 2008 Geoff Reedy 

Copyright 2015 Luben Karavelov
