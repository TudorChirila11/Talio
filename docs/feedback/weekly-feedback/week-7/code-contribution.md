# Code Contributions and Code Reviews

#### Focused Commits


Grade: Good

Feedback:

- You improved on how you make commits, the repo has a good amount of commits, but some are quite big (1c292761 which adds ~500 lines) 
- I also see and improvement in naming, even if sometimes you can still do better (there are cases where the message is a little too long and not concise e.g. c00ab393 ). A good example of commit is c59987a8. It has a descriptive message, a good amount of code commited, not too many files affected, clean code and javadoc. 
- As a **TIP**, try to make sure you build the code before commiting, I saw some examples of failing pipelines that could be avoided. (by running checkstyle or build)  Moreover, I still see a couple of fix up commits, which indicate that you do not test/review as you should before merging/pushing. Please keep an eye on this.


#### Isolation

Grade: Good

Feedback:

- The group uses feature branches/merge requests to isolate individual features during development. I think the quality of MR has improved, meaning that MR are targeted.
- I still see MR from branches with your names, which I specifically said not to use (MR !37) When you work with bux/fixes etc, you can always just make an issue for it and generate a branch with that name. I saw that you branches in general relate to the epic and not to issues specifically (which is not bad, but things could isolate even more, so you do not end up with huge MR) 

#### Reviewability


Grade: Very Good

Feedback:

- As I mentioned above, you improved the quallity of MR. MRs always have a clear focus that becomes clear from the title and the description, some nice examples are MR !45 or !33.
- The MR still contain some of commits for formatting changes. As a **TIP**, I recommend always running checkstyle and building the server before commiting. You could also merge main in your branch before making a MR in order to avoid conflicts. Also commit more often and small. This way you can check the code in small steps.


#### Code Reviews

Grade: Very Good

Feedback:

- The code reviewing has improved a lot since last time. Very well done.
- As a **TIP**, there are still some people that are not reviewing that much, for those, try to get more involved for the last period. As a team, you can help them by assigning them as reviewer for your MR. 


#### Build Server

Grade: Very Good

Feedback:

- In terms of build server, your group has committed/pushed frequently and chose their own checkstyle rules, which is very good.
- I still observed some fails of pipeline due to lack of attention to the checkstyle. **Make sure you run it before commiting** Same for tests and build.
- Moreover, some branches still have failing pipelines, but they are fixed directly, so good job.
