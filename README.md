# An AWS lambda function that rolls wild dice.

This app is an AWS lambda function that is designed to be the target of
a Slack slash-command.

It does a simple wild die roll as is featured in the Star Wars Role-Playing
game. This means that one die (if there are two or more dice being rolled)
is treated as a wild die: if it rolls the highest number possible (6 on a
d6, for example), then it continues to roll until it rolls a number other
than the highest number possible.
