# Scout
Scout helps to perform email search flawlessly on the basis of extracting various email search parameters from plain english query.

<img src="https://github.com/webianks/scout/blob/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" height="128" width="128" >

# Problem
Email search on the mail server is based on explicitly providing input parameters like subject,
date, from, to etc.
Task is to translate a plain English based search text to the provided input set of
parameters.

**Example**
<ul>
<li>mails from ravi in the last 3 days —&gt; From:- Ravi, ToDate:- Today, FromDate:- Today-3days</li>
<li>ppts/presentations from ravi to me and rohan -- &gt; From:- Ravi To:-(Me and Rohan), AttachmentType:- ppt</li>
<li>all attachments larger than 3MB —&gt; AttachmentSize &gt; 3MB</li>
<li>Citrix XenMobile document -&gt; AttachmentType:- ppt/doc/xls/txt/pdf, AttachmentName :-Citrix/XenMobile/Citrix XenMobile </li>
</ul>


# Search sentences break-down
All the search sentences should be broken down to the following parameters.

<ol>
<li>From</li>
<li>To</li>
<li>ToDate</li>
<li>FromDate</li>
<li>HasAttachments</li>
<li>AttachmentType</li>
<li>AttachmentSize</li>
<li>AttachmentName</li>
<li>Subject</li>
<li>CC</li>
</ol>

#Solution

As we studied the problem statement, we came to find out that this can be achieved by <b>Natural Language Processing.</b> We looked through all the options for NLP and decided to go with <b>IBM Watson Knowledge Studio</b> and <b>IBM Alchemy Language API.</b>
For a particular language, we deal with finding the different <b>entities</b> in it, which is obtained by <b>training the model</b> accordingly in Watson Knowledge Studio. The model is trained by the different <b>annotations</b> provided by us to <b>recognize different entities</b> such as <b>Username</b>, <b>Time</b>, <b>Attachment</b>, <b>From</b>, <b>Subject</b> etc. We provide a generalized <b>training set</b> to the <b>model</b> to learn it, then <b>deploy the model to alchemy API service.</b>
From our <b>Android App</b>, we <b>input the file</b> and make call for each line to the alchemy service which has already be trained, this returns us with a <b>JSON response</b> containing the entities and the order which they we’re detected.
Now we <b>process</b> through those <b>obtained entities</b> in the App, for a <b>pattern</b> and <b>draw deductions</b> for the required parameters and write the <b>output in the file.</b>


**An Example of entity recognition and processing**
	PDFs from Ravi in the last 3 days
  
Entities are follows (JSON Response):
```json
"entities":[ 		
{
"count": "1",
"text": "PDF",
"type": "ATTACHMENT_TYPE"
},			
{
"count": "1",
"text": "from",
"type": "FROM"
},			{
"count": "1",
"text": "Ravi",
"type": "USERNAME"
},		
{
"count": "1",
"text": "last",
"type": "SPAN"
},		
{
"count": "1",
"text": "3 days",
"type": "DATE"  
} ]
```




##License
```
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
```
