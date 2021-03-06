# CHANGELOG


All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog][1],
and this project adheres to [Semantic Versioning][2].

## [1.3.0] 2022-01-26
### Kukumo::CORE
#### Added
- Steps can use property substitution using the syntax`${property.name}`


## [1.2.1] 2021-10-22
### Kukumo::CORE
#### Fixed
- The `ClassLoader` now loads `StepsContributor` from maven artifacts.
- Now `RunnableBackend.runStep` catch `Throwable` errors instead of `Exception`.


## [1.2.0] 2021-09-28
### Kukumo::CORE
#### Added
- New property `childrenResults` in `PlanNodeSnapshot` that collects
  the result count of direct children.


## [1.1.0] 2021-09-17
### Kukumo::CORE
#### Added
- New field to `PlanNode` called `executionID`, that is set when the test plan is
  executed. The execution id can be either set via configuration (new property `executionId`) or
  autogenerated otherwise. The same id is shared along every node in the plan and it should be
  unique among different executions. The aim of this new field is provide a way to distinguish
  executions when they are run in parallel in a server.
- New method `KukumoContributors#allContributors()` that returns every contributor
#### Modified
- Assertion-like data types now implement their own interface `Assertion` rather than
  using `Matcher` from the *Hamcrest* library. The `MatcherAssertion` class allows mapping to
  `Matcher` for convenience.
- Assertion literals `matcher.string.null` and `matcher.number.null` merged into
  `matcher.generic.null` since the translated literals are likely to be the same
#### Fixed
- The contributor `ConfigContributor` now extends from `Contributor` (as it should do originally)


## [1.0.0] 2019-04-03

Initial release.


[1]: <https://keepachangelog.com/en/1.0.0/>
[2]: <https://semver.org>