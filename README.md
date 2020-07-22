# Journey
Repo for FBU Engineering final app

A self-help/guided-journaling app that will take you on a personal journey to self improvement.


## App Idea Brainstorming
Journey - A New Approach to Self Improvement
    - Start by outlining the traits you want to have/embody, and track your daily progress towards achieving those goals. Basically a journaling app that is focused on self improvement. For example, a trait the user could pick would be "Develop stronger relationships with my friends." To accomplish this, they will be taken through the following process:
        - What things do you currently do that prevent you from developing stronger bonds?
        - What things would someone do to develop stronger bonds?
        - What is a small and repeatable step you can take towards developing stronger bonds?
        - etc.
    - Basically, this would be a sort of template for self improvement. I could base the structure off of optimal techniques (will have to do more research).
   
   
## Wireframes

https://www.figma.com/file/qClIizmXb2I8LTkHtXK8Du/Journey?node-id=0%3A1

## Progress Outline

# Week 1
- [X] Create data models
- [X] Build skeleton views
- [X] Implement navigation between views + bottom navigation bar
- [X] Setup Firebase for user authentication
    - [X] Firebase offline
- [X] Review and update project plan
- [X] Setup Google Maps SDK

# Week 2
- [X] Create functionality for making new journal entries
    - [X] Setup prompts data model with firestore
    - [X] Implement the transition between different prompts
    - [X] Implement the aggregation of data amongst all answered prompts
- [X] Implement basic entry detail generation
    - [X] Handle the aggregation of geotags
    - [X] Handle the display of saved media
- [X] Setup all recyclerviews to populate now that data is available
    - [X] Setup timeline RV
- [ ] Review and update the design of analysis entries to prepare for week 3
    - [ ] Decide upon/implement the optimal data model to handle analysis
    - [ ] Setup the RV for analysis
    - [ ] Setup analysis data in firestore
- [X] Implement log out button


# Week 3
- [ ] Visual polish
    - [ ] Include an animation when signing in
- [ ] Create analysis feature
- [ ] Implement detail views
    - [ ] Detail view for analysis
    - [X] Detail view for entries
- [ ] Add gesture recognizer for favoriting entries by double tapping them
- [ ] Add any extra journaling "tracks"

# Week 4
- [ ] Implement 3 full tracks
- [X] Allow users to create new journals
- [ ] Extra visual polish
- [ ] Add extra polish to UX
- [ ] Stretch Goals
    - [ ] Consider new ways to enhance the analysis feature
    - [ ] Integrating spotify/music clips into Journal entries
    - [ ] Adding any additional tracks (max 5)
    - [ ] Allow users to run reports across various journals
