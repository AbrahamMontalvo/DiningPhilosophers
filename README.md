# The Dining Philosophers Problem

**By Abraham Montalvo and Abigail Brown**
<br>
Group Project for CS-410

## High Level Description

The Dining Philosopher's Problem states that there are _k_ philosophers seated around a table. These philosophers live
out their life in happy thought. However, they occasionally get hungry. In order to eat the rice set in front of them,
they must have 2 chopsticks available to use: one on their left and one on their right. However, for every _k_
philosopher, there is only 1 chopstick. This means that the philosophers must share their chopsticks with their
neighbors. This can thus result in a deadlock situation, where one hungry philosopher never relinquishes their chopstick
to their neighbor, or even starvation, where the philosophers are unable to ever eat and thus are starved.

![An image depicting the Dining Philosopher Problem.](img/TDP.png)

The goal of this project is to develop a coded solution that will solve potential deadlock and starvation scenarios. The
following code best provides a solution to the infamous dining philosopher problem.

## Overview of the Code

![A UML Diagram Depicting the Code](DiningPhilosopher%20UML%20-%20BrownMontalvo.png)

The UML diagram above depicts the two main classes used for our project submission.

The Philosopher class uses a thread-running method to control the behavior of each Philosopher instance.
It is synchronized on the class itself to share the total chopsticks available at the table throughout the running of
the program.

The Random Philosopher class is nearly the same as our formal submission Philosopher class, but randomly selects the
philosopher to eat.

## Challenges

Throughout the development process, we encountered a couple major challenges. The first challenge we encountered dealt
with starvation. Our first threaded solution had a bug that made it so that the same philosopher would grab the
chopsticks and eat, but left the others to starve despite having other chopsticks available. In the process of trying to
fix this bug, we ended up accidentally making it so all philosophers stopped eating entirely and were only thinking (and
starving). After some trial and error, we were able to modify the synchronization and queueing strategy so that
philosophers were taking turns eating and thinking.

## Resources Used

* ChatGPT for minor debugging (marked in the code)
* [Geeks for Geeks - Semaphores](https://www.geeksforgeeks.org/dining-philosopher-problem-using-semaphores/)
* [Medium Article](https://medium.com/@ruinadd/philosophers-42-guide-the-dining-philosophers-problem-893a24bc0fe2)

## Presentation Slides

For more information about this project and the challenges faced during development, please visit our Canva presentation
using the link below:

* [Presentation Slides](https://www.canva.com/design/DAGkR6CFumk/9Y_MKEk8M-kvNBKgNn2W1w/edit?utm_content=DAGkR6CFumk&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton)
