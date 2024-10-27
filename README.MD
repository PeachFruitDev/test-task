# Reporter Project

Here is a reporter project to help analyze organization's structural issues.
It is made in form of a simple console Java application.

# Overall

During the development I focused on iterative TDD approach. **_It can be seen in commit history_**.
To ensure testability I decided to follow more OOP approach and don't use static methods and constants, as well as
creating intermediate objects for reports.

# Assumptions

Some assumptions were made:

1. It's a small app, which possibly can be extended later, but there is no need to overengineer and make it too much
   abstract, thus:
   1. There is no interfaces like Parser or Reporter, code was made that way, so it's easy to create common interfaces
      or abstract classes. However, I decided to avoid interface creation for now.
   2. There is no templates made for toString() methods in Reports, not to overcomplicate. And reports are fully
      separated
      (they could be used as inherited classes from a united abstract class).
2. Validity of the results is the main concern:
   1. TDD was prioritized as an approach, test first, then code.
   2. More test for Parser to ensure validity of incoming files. And tests for the results of business logic.
3. Assumed, employees are ordered the way managers of an employee come before the employee. Why:
   1. That prevents cyclic hierarchies, where reporting line is circled.
   2. That makes easier creation of an employee tree.
   3. If we will ever need to connect an RDB and work with it. That ensures we create referenced (by managerId) entities
      before the reference itself.
   4. **TODO (Optional)** Create a sorter which first sort the list in-memory (it's affordable as we have <1000
      employees)
      so managers come first.

# Decisions

1. It was decided to create error list in parser, which links to the line with an error in CSV, so fixing could be
   easier
   for an end-user.
2. Usage of a prop file to configure and add analysis params with ease.
3. Only Unit tests as they are enough to test functionality of such a small app.
4. Not to use Gitflow and push to master, as I work alone on a small project.