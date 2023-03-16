# Code Contributions and Code Reviews

#### Focused Commits

Grade: **Insufficient**

Feedback: 
- For this week some of members don't have commits at all. Please make sure you are aware that we run our gitinspector with 24 hours before the meeting. I saw you had some commits and merge requests on the very last moment and for this time I will still consider them.
- The commits that you have are having a good amount of code and a clear target. An example of this is commit 98bb2e98. The description of what the commit is doing is usually very general. You could try formulate them like (This commit) "Adds GUI, assets and stylesheet for a board. It also adds a basic controller."
- As a **TIP** try to make sure everybody has meaningful commits related to small individual changes. Your commit messages should be descriptive but still concise. Always document your code in time. You should not commit just comments or checkstyle changes too often. Also make sure your commits are tested and run the build before, I see you have a failing pipeline most of the time.


#### Isolation

Grade: **Insufficient**

Feedback:
- You started working multiple features (board and card) on a dev branch which violates the principle of isolation. The idea is that you should use feature branches for your work and make MR targeted directly to main. As a TA I am supposed to look in general at the MR you perform to main.
- As a **TIP** try to make sure you name your branches descriptive per feature or per change. A bad example is MR 8 because the description is very general (same for name). What is exactly card feature? Could you relate that to the user story or epic name? In the description you could make a list of things added by this MR (e.g controller for basic card functionality, UI for displaying a card, etc). I also recommend to test + document the code before merging it. 
- As a **TOP**, the commits of the MR 8 for example are small enough and related to each other. The MR adds a reasonable amount of code and changes.

#### Reviewability

Grade: **Insufficient**

Feedback:
- Your MR are usually not that reviewable since they lack descriptions (constructive ones), making it hard for the reviewers to understand what changes are introduced by the MR.
- As a **TOP** I like that you have java doc in the MR 8 for example. That can help with the flow in case of a future review.
- As a **TIP**, try to make the MR clear, focused, with good descriptions so they are easy to review. The changes should not be related to other features and always solve merge conflicts. Please always let other students approve your MR, do not approve your own!!


#### Code Reviews

Grade: **Insufficient**

Feedback:
- Even in the small nr of MR you performed, there are not reviews at all.A comment of "nice work" or "looks good to me" are not constructive.  
- As a **TIP** try to make sure everybody reviews at least 1 MR in a constructive way. Keep the discussions on point and always involve more members in more controversial changes. Do not leave your MR open for too long. Merge small and often so the code is checked frequently. When performing a MR always respond to the reviews given by your collegues.



#### Build Server

Grade: **Insufficient**

Feedback:
- This part is related to the building of the Server, and you run the server often, but has a lot of fails.
- As a **TIP** make sure you commit frequently so the pipeline runs often. You should not have a failing pipline especially on main. In case of a failing pipline fix it as soon as possible and test your code by building it before pushing. Make sure your checkstyle rules are followed (run it before pushing), since they can also lead to failing pipelines. You should not have commits that change the formating too often (so basically repairing the checkstyle you forgot to run). Make sure you choose your own checkstyle rules.

