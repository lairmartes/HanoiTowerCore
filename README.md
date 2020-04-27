# HanoiTowerCore
Components to be used in Hanoi Tower games. Developers can use it to build the application.

# Main Components
Pin, Disks, Game Control and events that notify client object about what happens in the game.

# Services
## Start Game
Use it to start the game.  It's possible to provide how many disks the game will have.  It will launch a __Game Start__ event.

## Restart Game
Start a new game.  Use it when a game is already running and needs to restart another one anytime. Provide how many disks the new game will have.  Will launch a __Game Start__ event.

## Grab Disk
Get the disk located in the top of the given pin.  Will launch a __Disk Removed__ event.

## Drop Disk
Drop the disk grabbed in the given pin if the the pin is empty or if the disk in the pin is bigger than the disk being dropped.  Will launch a __Disk Added__ event.

## Events
__Game Over__: Launched when the game ends (i.e.: all disks from left pin are moved to right pin).
