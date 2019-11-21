@DbTest
Feature: ICT-10357

  Scenario: [ICT-10357] bmmv_obsunitset table has many ous missing - protrack
    When Verify xml_schedblock_entities and bmmv_schedblock are consistent
      |select count (*) from (select ARCHIVE_UID from xml_schedblock_entities minus select ARCHIVE_UID from bmmv_schedblock)|


