# Clean-Code Web-Crawler Project

This project is a web-crawler for the Clean Code course at the AAU Klagenfurt. It is written in Java and can be used to
crawl a website to generate a markdown file containing the translated headings of the website and the outgoing links (
which will be crawled aswell, depending on the given depth). It will also identify broken links and provide proper
identation
based on the depth of the website (in regards to the root website)

## Features

The crawler has the following features:

1. **Command Line Interface**: After starting the application, the user is guided through the command line to enter the
   necessary parameters like URL, depth, domains and the target language.
2. **Colorized-Logs**: By default the logs of the Crawler are colorized to make it easier to read. This feature can be
   disabled by setting the static `colorizeLogs` variable in the `Console` class to false.
3. **Translation**: The crawler creates a compact overview of the crawled websites in a specified target language
4. **Headings**: The crawler records and translates the headings of the website.
5. **Depth Representation**: The crawler represents the depth of the crawled websites with proper indentation.
6. **Conccurency**: The crawler is able to crawl multiple pages concurrently.
7. **URLs**: The crawler writes down the URLs of the crawled websites.
8. **Broken Links**: The crawler highlights broken links, thus pages that are empty.
9. **Recursive Analysis**: The crawler finds the links to other websites and recursively crawls them as long as they are
   in the depth range set by the user.
10. **Result**: The crawler stores the results in a single markdown file.

## How To Run

This project is a Gradle project and can be build and run like a normal Gradle project. However since the translations
are made through the [Text Translator](https://rapidapi.com/dickyagustin/api/text-translator2/) API from RapidAPI it is
required to have an API key set for the translation to work.  
The API key must not be set as an environment variable `API_KEY` in your IDE, since the translation has been disabled to
the rate-limitations of the free-plan. Please note that when crawling a website using a high depth or the website has
many headings, the free tier of the API might be used up quickly due to the amount of translation calls made.

To execute the crawler, run the `main`-method in the `Main`-class and follow the prompts in the command line for the
needed parameters

## Testing

This project uses JUnit for automated unit tests. The tests can either be run separately or by opening the project and
running the following command:   
```./gradlew test```

## Dependencies

This project uses the [jsoup](https://jsoup.org/) library for parsing HTML,
RapidAPI [Text Translator](https://rapidapi.com/dickyagustin/api/text-translator2/) API for translating the headings and
the [JSON in Java](https://mvnrepository.com/artifact/org.json/json) package.
