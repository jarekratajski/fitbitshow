# FitbitDemo

## prerequisites:
- jdk 8,
- maven,
- npm
- dev.fitbit.com
  (You need to registered application (it is free), [callback : http://localhost:8088/services/after, type: Personal]) .


## Run server
First create new file in:
 *fitbit-demo/hello-impl/src/main/resources/fitbit.properties*
 put one line:
 *secret=blablasecret*
 ( where *secret* comes from dev.fitbit.com).

Replace in sources text *27S5Y* with your application ID.

* cd fitbit-demo
* mvn lagom:runAll

## Run client
* cd fitbit_web
* npm install
* gulp
* gulp dev




## Disclaimer
For walking man animation I used great tutorial and sample code from :
Andre Hoyer
https://github.com/ndrwhr/css3-walking-man
(LICENSE MIT)


