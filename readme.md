CanDoGram
---------

Currently, CanDoGram is a Java-EE wrapper around the excellent `java-telegram-bot-api` by `com.github.pengrad` - check it out [here](https://github.com/pengrad/java-telegram-bot-api).

It's not currently wrapping the most recent version (I'm pretty sure), but it does everything I've needed recently.

On the soon to-do list is support for games, but many other things too.

On the longer to-do is to wrap other messaging libraries - the API is currently obviously wanting to tend in that direction,
but if I ever get there the bot API will probably have to be tweaked a fair bit. Specifically, the inline button functionality 
is currently using Pengrad objects, which obviously won't work.

This artifact doesn't sit in Maven central yet, so you'll have to compile it locally, but that shouldn't be tricky.

For a super basic example of what it can do, check out [HelloCanDoGram](https://github.com/EvanKnowles/HelloCanDoGram). 

The library supports more complicated, conversation-style discussions (invite @zaFoolBot to a conversation to see complicated commands), 
but I'll totally get to examples of those at some point.