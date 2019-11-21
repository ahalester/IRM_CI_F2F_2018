/* Disable constraints on aqua_ous table */
alter table aqua_ous disable constraint aqua_ous_finalcomment_fk;

/* Disable constraints on asa_delivery_asdm_ous table */
alter table asa_delivery_asdm_ous disable constraint fk_dp_delivery_asdm;
alter table asa_delivery_asdm_ous disable constraint deliveryid_fk;

/* Disable constraints on asa_delivery_status table */
alter table asa_delivery_status disable constraint dp_deliverystatus_account_fk;

/* Disable constraints on asa_energy table */
alter table asa_energy disable constraint asa_energy_science;

/* Disable constraints on asa_science table */
alter table asa_science disable constraint fk_goalobs_uid;
alter table asa_science disable constraint fk_groupobs_uid;
alter table asa_science disable constraint fk_memberobs_uid;
alter table asa_science disable constraint fk_projectcode;
alter table asa_science disable constraint fk_projectuid;

exit
