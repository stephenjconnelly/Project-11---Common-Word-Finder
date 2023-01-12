Stephen Connelly Sjc2235

I would expect that the Hash table would be the fastest and BST the slowest. I assumed this because the traversal and
get() methods of both AVL and HashMap are faster than that of BSTMap. However, my results do not reflect this. AVL is
actually slower than BST (and hash is still fastest). While it is true that iteration and get() using an AVL is
faster than that of the BST, because I used put() extensively in my code, AVL is slower. This is because for
implementations that use put() or delete() a lot, the AVL tree has to make many rotations, and can end up being slow because of its
balancing. However, it should be noted that when iterating through AVL and BST, I did not have to sort afterwards
numerically. This was not the case for HashMap as I had to sort twice. However, HashMap still prevailed because of its
efficiency with an average time complexity of theta(1) for searching, iterating, and deleting.

P.S. Thank you TAs for all your help this year. It was a pleasure and I hope to see some of next semester in AP. All
the best.

Stephen.