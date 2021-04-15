# lab 4 :coffee:

The secret message is a famous secret message from *The Gold-Bug*

The decrypted message is 

> a good glass in the bishop's hostel in the devil's seat twenty one degrees and thirteen minutes northeast and by north main branch seventh limb east side shoot from the left eye of the death's head a beeline from the tree through the shot fifty feet out

### How did I solve this lab?

Not depth first search but manually put the possible letter one by one like this:`secondMap.put('(', 'r');`, which means `(` is `r`

This is derived by `;(88` to be `t*ee`, this seems to be `tree`

Then I print out the current encrypted and decrypted messages, and look for the next manually. It seems silly, but this is really fast

