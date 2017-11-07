# News-Feed
### Robert Farinelli 01402045
### Christopher Pearce 00892021
### Jacob Rothmel 01322102

### Android application that presents the top results from Google News with an analysis of keywords and Wikipedia links

![Alt text](https://github.com/uml-app1-2017/assignment-6-news-app-cp0153/blob/master/screenshot1.png?raw=true "Optional Title")

![Alt text](https://github.com/uml-app1-2017/assignment-6-news-app-cp0153/blob/master/screenshot2.png?raw=true "Optional Title")


### notes on api endpoints
https://newsapi.org/google-news-api

https://newsapi.org/v1/articles?source=google-news&sortBy=top&apiKey=23b2fa848a2a45aa85546b463a7afc0a

POST https://language.googleapis.com/v1/documents:analyzeEntities?key=AIzaSyAh9uz0qNveHuiNYNBhjanf5gq86Su5rlo
 
{
 "document": {
  "content": "This week’s dispute over President Trump’s phone call to the widow of a soldier killed in Niger hit too close to home for the White House chief of staff.",
  "type": "PLAIN_TEXT"
 }
}
 
