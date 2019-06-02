## juice-shop - performance test suite

Project uses [sbt plugin][sbtplugindoc] of [gatling][gatlingdoc].

### Run

All tests:
```
sbt "gatling:test"
```

Single test:
```
sbt "gatling:testOnly com.testfeed.CheckoutSimulation"
```

Report:
```
sbt "gatling:lastReport"
```
