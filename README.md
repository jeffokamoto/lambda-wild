# An AWS lambda function that rolls wild dice.

This app is an AWS Lambda function called by the AWS API Gateway that is
designed to be the target of a Slack slash-command.

It takes as input a "roll spec" string, which is composed of either
numbers or any number of "die spec" strings, which are of the form
"nDm", which means to roll "n" number (default 1) of "m"-sided dice
(required).

Instead of a "D", a "W" may be specified. This means that the dice
should be considered wild (in the context of the Star Wars Role-Playing
game), or exploding (in the context of Savage Worlds). Any dice that
roll their maximum value are continued to be rolled until a non-maximum
number is rolled, and all the rolls are then added up.
