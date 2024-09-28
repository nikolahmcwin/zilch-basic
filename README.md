Simple Java project to play 'Zilch' dice game in the terminal.

-----
Overview
-----
2 players minimum.
6 six-sided dice.
Each player takes turns throwing the dice, collecting points, and banking those points to the scoreboard.
Different value dice have different points.
First to 10,000 points wins.
If you use all six dice in your turn without losing, they return to you and you get to roll them all again, continuing your points collection.

----
Scoreboard
----- 
No points or zilches are recorded for a player until that player enters the scoreboard.
Minimum score start of game - 1000 points.
Minimum score after entering scoreboard - 350 points.
If you have met the minimum points, you can chose to end your turn by banking them.
If you don't have the minimum points, you must keep rolling.
If you get no points you get a 'Zilch' instead.
If you get three zilches in a row you lose 1000 points.

-----
Points
-----
All six dice:
    One of each - 1500 points
    Three pairs - 1000 points
Trio of three dice:
    3 x 1s      - 1000 points
    3 x 6s      -  600 points
    3 x 5s      -  500 points
    3 x 4s      -  400 points
    3 x 3s      -  300 points
    3 x 2s      -  200 points
Single dice (always):
    1 x 1s      - 100 points
    1 x 5s      -  50 points
Single dice (with same triple)
    1 x trio #  - 100 points

