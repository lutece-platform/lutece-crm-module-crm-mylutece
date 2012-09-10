
--
-- Dumping data for table core_admin_right
--
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url) VALUES 
('CRM_MYLUTECE_MANAGEMENT','module.crm.mylutece.adminFeature.crm_mylutece_user_management.name',1,'jsp/admin/plugins/crm/modules/mylutece/ManageCRMUsers.jsp','module.crm.mylutece.adminFeature.crm_mylutece_user_management.name',0,'crm-mylutece','USERS','images/admin/skin/plugins/crm/modules/mylutece/crm-mylutece.png', NULL);

--
-- Dumping data for table core_user_right
--
INSERT INTO core_user_right (id_right,id_user) VALUES ('CRM_MYLUTECE_MANAGEMENT',1);
INSERT INTO core_user_right (id_right,id_user) VALUES ('CRM_MYLUTECE_MANAGEMENT',2);
