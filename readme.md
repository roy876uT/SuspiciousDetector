## Suspicious trade detector

### Candidate
Name: Roy Tam  
assignment complete date: 19 Nov 2024
The task was completed on my own and without any AI tool assistance.

### Class description

| Class                | type                                | description                                                                                                                                           |
|----------------------|-------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|
| SuspiciousDetectable | Interface                           | Detector execution entry. It contains one method: it takes a list of trades and a list of orders, and outputs those of them which it finds suspicious |
| SuspiciousDetector   | SuspiciousDetectable implementation |                                                                                                                                                       |
| Testable             | Functional Interface                | Suspicious test                                                                                                                                       |
| OppositeSideOrder    | Testable implementation             | in a time, window of 30 minutes before the trade, there were placed order(s) of an opposite side.                                                     |
| DeviateOrder         | Testable implementation             | the orders you are checking for the trade have a price not more than 10% lower or higher than the trade price                                         |
| DetectionResult      | Java record                         | test result only contains suspicisous trade & order                                                                                                   |
| SuspiciousReason     | Java enum                           | Test result failed reason                                                                                                                             |
| Order                | Java record                         | model                                                                                                                                                 |
| Trade                | Java record                         | model                                                                                                                                                 |
|                      |                                     |                                                                                                                                                       |

### Program flow
1. Detection starts with SuspiciousDetectable's test method. 
2. It loops through 2 rules: OppositeSideOrder and DeviateOrder. Each rule applies test on all related trade and order, which are mapped by key. 
3. Result is returned in form of DetectionResult list 

### Suspicious test

| rule | description                                                                                                                                                                                                                             |
|------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| OppositeSideOrder     | in a time, window of 30 minutes before the trade, there were placed order(s) of an opposite side                                                                                                                                        |
| DeviateOrder     | the orders you are checking for the trade have a price not more than 10% lower or higher than the trade price        <br/> - support positve & negative price range<br/>- Assumption: If the price is zero, expected 10% range is zero. |

### Recommendation
Java primitive type double has limited precision. It might suffers from rounding errors when dealing with decimal fractions.
If loss of precision is a concern, Java BigDecimal is recommended. 

### Unit Test
All unit tests are in SuspiciousDetectorApplicationTests.java

#### coverage
- 30 min opposite before trade test
- 30 min same side before trade test
- opposite order appeared at 30 * 60+1 second before trade
- multiple 30 min opposite before trade test
- multiple 30 over min opposite after trade test
- positive order price deviate with 10% of trade price
- negative order price deviate with 10% of trade price
- zero order price deviate with 10% of trade price
- mix of 2 test

#### How to run?  
There are 2 ways.
1. Execute by maven. Go to project directory
```
.\mvnw test
```
2. Execute within Intellij  
- right click SuspiciousDetectorApplicationTests.java  
- click run 'SuspiciousDetectorApplicationTests'

### Tool
- Java 21
- Intellij
- Maven
- Java libary: lombok, junit 5 