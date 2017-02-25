# Scout
Scout helps to perform email search flawlessly on the basis of extracting various email search parameters from plain english query.

<img src="https://github.com/webianks/scout/blob/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" height="128" width="128" >

# Problem
Email search on the mail server is based on explicitly providing input parameters like subject,
date, from, to etc.
Task is to translate a plain English based search text to the provided input set of
parameters.

Example:

o mails from ravi in the last 3 days —&gt; From:- Ravi  ToDate:- Today FromDate:- Today-3days
o ppts/presentations from ravi to me and rohan -- &gt; From:- Ravi To:-(Me and Rohan) AttachmentType:- ppt
o all attachments larger than 3MB —&gt; AttachmentSize &gt; 3MB
o Citrix XenMobile document -&gt; AttachmentType:- ppt/doc/xls/txt/pdf   
AttachmentName :-Citrix/XenMobile/Citrix XenMobile

##License

MIT License

Copyright (c) 2017 Ramankit Singh : Copyright (c) 2017 Sajal Gupta

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
