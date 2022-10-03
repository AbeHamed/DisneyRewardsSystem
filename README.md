# DisneyRewardsSystem
A program that takes in two customer files, and one order file. The customers in the "regular" file will not be in any rewards programs yet. The customers in the "preferred" file will either be considered Gold or Platinum.

The orders file will have all orders starting with the Guest ID of a matching customer from the files. 
Each line of the orders file will be interpreted, and implemented onto each customer's profile. If a customer reaches a certain threshold, they will be promoted to Gold or Platinum.

Gold customers get either a 5%, 10%, or 15% discount depending on how much they have spent. Platinum customers get "Bonus Bucks", or rewards points that can be used towards the next purchase.

The program features a dynamic array, meaning excellent memory usage. A regular array will constantly be updated to match the size of the customers it holds. It will never be too long, and does not use an Array List!
