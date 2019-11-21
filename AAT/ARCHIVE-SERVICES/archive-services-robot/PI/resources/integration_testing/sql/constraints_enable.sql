/* Enable constraints on aqua_ous table */
alter table aqua_ous enable constraint aqua_ous_finalcomment_fk;

/* Enable constraints on asa_delivery_asdm_ous table */
alter table asa_delivery_asdm_ous enable constraint fk_dp_delivery_asdm;
alter table asa_delivery_asdm_ous enable constraint deliveryid_fk;

/* Enable constraints on asa_delivery_status table */
alter table asa_delivery_status enable constraint dp_deliverystatus_account_fk;

/* Enable constraints on asa_energy table */
alter table asa_energy enable constraint asa_energy_science;

/* Enable constraints on asa_science table */
alter table asa_science enable constraint fk_goalobs_uid;
alter table asa_science enable constraint fk_groupobs_uid;
alter table asa_science enable constraint fk_memberobs_uid;
alter table asa_science enable constraint fk_projectcode;
alter table asa_science enable constraint fk_projectuid;

exit
