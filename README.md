# Web Application Layouts with Freemarker, WebJars & Bootstrap

## Overview

When building a web app you’ll likely want to have a consisted layout and navigation across your site. For internal applications, frameworks like [Bootstrap](http://getbootstrap.com) make it easy to have something attractive and consistent out of the box.

Front-end developers use tools like [Bower](http://bower.io) to automatically manage the frameworks, libraries and assets they use in their projects. A similar format called [WebJars](http://www.webjars.org) exists for Java web apps which packages client-side libraries into JAR files.

We’re going to create a couple of simple pages that use the same layout by making use of [Freemarker](http://freemarker.org)’s macros with Bootstrap provided via a a WebJar.

## First Steps

First we create a Spring web app with Freemarker as a dependency

    $ spring init web-layout -d=web,freemarker

Then we create a simple controller that serves up two pages:

    package demo;

    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;

    @Controller
    public class PagesController {
        @RequestMapping("/one")
        public String one() {
            return "one";
        }

        @RequestMapping("/two")
        public String two() {
            return "two";
        }
    }

Next, we create a template for each of the actions. Here is page one:

    <p>This is page one.</p>

And here is page two:

    <p>This is page two.</p>

If we start up our server, we can get to the two pages by going directly to their URLs.

    $ mvn spring-boot:run

- <http://localhost:8080/one>
- <http://localhost:8080/two>

To make these pages look nicer and consistent, we’ll add Bootstrap and create a layout Freemarker macro.

## Adding Bootstrap with WebJars

The [WebJars web site](http://www.webjars.org) makes it easy to search for a project and copy the dependency syntax needed by your build tool. We switch the build tool to Maven and then search for bootstrap. We then copy that XML into our `pom.xml`.

    <dependency>
        <groupId>org.webjars</groupId>
        <artifactId>bootstrap</artifactId>
        <version>3.3.5</version>
    </dependency>

To create the macro we’ll make a `layout` directory under the `templates` directory and create a Freemarker template named `application.ftl`. Inside this file we define a macro named `layout` and add the HTML for the layout of our site.

    <#macro layout>
    <!doctype html>
    <html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Example</title>
        <link rel='stylesheet' href='/webjars/bootstrap/3.3.5/css/bootstrap.min.css'>
    </head>

    <body>
        <nav class="navbar navbar-default">
            <div class="container">
                <div id="navbar" class="collapse navbar-collapse">
                    <ul class="nav navbar-nav">
                        <li><a href="/one/">One</a></li>
                        <li><a href="/two/">Two</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <div class="container">
            <h1>Example</h1>
            <#nested>
        </div>
    </body>
    </html>
    </#macro>

Since we want each page to provide its own contents to the layout, we use the `<#nested>` directive to render the page itself within the layout.

To make it easy to the macro on every page in our app, we can configure Freemarker to auto include the template with this layout macro. In `application.properties` we set:

    spring.freemarker.settings.auto_include=layout/application.ftl

Now we can update our page templates to make use of the macro. Here is page one:

    <@layout>
        <p>This is page one.</p>
    </@layout>

And here is page two:

    <@layout>
        <p>This is page two.</p>
    </@layout>

If we restart our server and check out our pages, they should have the same look and feel now.

    $ mvn spring-boot:run

## Page Titles & Headers

Currently both pages have the same title and header. It would be nice if we could set additional content for each page other than the main body.

We can go back into our macro and add a parameter for the title and then reference that in our layout.

    <#macro application title>
    <!doctype html>
    <html lang="en">
    <head>
        <meta charset="utf-8">
        <title>${title}</title>
        <link rel='stylesheet' href='/webjars/bootstrap/3.3.5/css/bootstrap.min.css'>
    </head>

    <body>
        <nav class="navbar navbar-default">
            <div class="container">
                <div id="navbar" class="collapse navbar-collapse">
                    <ul class="nav navbar-nav">
                        <li><a href="/one/">One</a></li>
                        <li><a href="/two/">Two</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <div class="container">
            <h1>${title}</h1>
            <#nested>
        </div>
    </body>
    </html>
    </#macro>

Then we assign a value to that parameter when we use the macro in each page. Here is page one:

    <@layout title="One">
        <p>This is page one.</p>
    </@layout>

And here is page two:

    <@layout title="Two">
        <p>This is page two.</p>
    </@layout>

## Resources

- http://freemarker.org
- http://freemarker.org/docs/ref_directive_macro.html
- http://getbootstrap.com
- http://www.webjars.org
