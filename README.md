# travelblog
##### Table of Contents
[Overview](#overview)  
[Why did I write this?](#why-did-i-write-this)  
[How do I run it?](#how-do-i-run-it)  
[What hosting solution do you use?](#what-hosting-solution-do-you-use)    
[How has the application been designed?](#how-has-the-application-been-designed)  
[Why XML not Json?](#why-xml-not-json)  
[Static analysis and testing](#static-analysis-and-testing)

### Overview

Java application to convert a diary maintained in xml into static HTML: 

![travel-blog.png](travel-blog.png)

### Why did I write this?
I have maintained a blog of our travels in XML since the late 90s. 
Originally I used php to convert the XML at an ISP. But then in early 2020, 
irritated by rising prices for hosting at an ISP trying to force me to use Wordpress, I decided to switch to a much simpler and cheaper 
hosting solution based on static HTML. So I wrote this application to convert my files
in a static fashion prior to uploading.

### How do I run it?
Firsly build using `mvn install`. When executing it all the arguments can be specified on the command line each time, 
but I find it easier to create a file called `travel-blog.properties` with all your keys in:

```text
--googlekey
<your google api key>
--url
<canonical url, the url that you want Google to index e.g. https://www.jacobmetcalf.co.uk>
--amazonkey
<optional amazon associates key>
--linkedin
<optional LinkedIn id, e.g. jacobmetcalf>
--facebook
<optional facebook account, e.g. jacob.metcalf.98>
--github
<optional github account, e.g. JacobMetcalf>
-r 
```
Then this can be executed as follows, where the `@` argument is the properties file you just created
and`<dir>` is the directory where the xml files to be processed can be found:

```text
java -p target/travel-blog-1.0-SNAPSHOT.jar:target/lib 
  -m uk.co.jacobmetcalf.travelblog/uk.co.jacobmetcalf.travelblog.Main 
  @travel-blog.properties 
  <dir>
```

**Note:** The above module path is ugly. I am currently researching jlink
as a solution to package but it does not deal with auto-modules out of the box.

### What hosting solution do you use?
The approach I use at https://jacobmetcalf.co.uk is pretty simple and cheap:
 * A storage bucket at Google Cloud. I am over the 5GB free tier but even then it costs less than a pound a month.
 * Storage buckets can be exposed as an IP address via http only.
 * DNS proxies at Cloudflare to expose as https and host the certificates. This is completely free.

### How has the application been designed?
There are three components to the application:
 * **/model**: This is a POJO model of the diary format. 
   I use [immutables.io](https://immutables.github.io/) to decrease the boiler plate here.

   
 * **/xmlparser**: Parses an xml input stream into the POJO representation. 
   This uses the JDK's XMLEventReader to power a pull parser.
   

 * **/htmlrenderer**: Uses [xmlet.org](https://github.com/xmlet) to render the model to HTML. 
   This was the most experimental part of the design. Xmlet provides a Java dsl for constructing
   HTML whilst ensuring it is correctly formed.

For those that are interested I found xmlet.org by way of [htmlflow.org](https://htmlflow.org/). 
Whilst htmlflow provided the DSL I wanted I struggled with dynamic views. It appeared that whilst
good for performance you lost the compile time enforcing of correctness. So not needing the 
performance I just used the underlying HTML5 dsl and wrote my own element writer.

### Why XML not Json?
Mostly because json did not exist in the 90s! However XML does offer one key advantage: support for mixed content,
which proves invaluable when trying to enrich text with links and other content.

### Static analysis and testing 
Uses:
 * Google [Errorprone](https://errorprone.info/): For static code analysis.
 * There are a suite of Junit5 tests.
 * I am currently experimenting with PITest to improve the quality of testing
