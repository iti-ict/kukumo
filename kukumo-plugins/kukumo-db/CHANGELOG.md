# CHANGELOG


All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog][1],
and this project adheres to [Semantic Versioning][2].

## [1.3.0] 2022-01-26

## Modified
- The where clause is moved from a parameter to document in the following steps:
  - `db.assert.table.exists.sql.where`
  - `db.assert.table.not.exists.sql.where`
  - `db.assert.table.count.sql.where`


## [1.2.1] 2022-02-07

### Fixed
- Setting `database.enableCleanupUponCompletion` to `true` no longer throws an exception


## [1.2.0] 2022-01-03

### Modified
- Version aligned with `kukumo-core:1.2.0`
### Fixed 
- When no primary key is found, an error is thrown to prevent all records being deleted.


## [1.1.0] 2021-09-17

### Modified
- Version aligned with `kukumo-core:1.1.0`
- Use of the `jsqlparser` library
### Fixed
- Fixed some step translations

  
## [1.0.0] 2019-04-03

Initial release.  


[1]: <https://keepachangelog.com/en/1.0.0/>
[2]: <https://semver.org>