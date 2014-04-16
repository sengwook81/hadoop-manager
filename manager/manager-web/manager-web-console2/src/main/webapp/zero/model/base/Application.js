/**
 * 
 */
Ext.define('Zero.model.base.Application', {
	extend : 'Ext.data.Model',
	fields : [ 'del_Yn','app_Id', 'app_Name', 'app_View_Id', 'app_Ver', 'cluster_Yn'],
	
	
//	
//	hasMany : [ {
//		name : 'configs',
//		model : 'Zero.model.base.ApplicationConfig',
//		associationKey : 'configs',
//	} ]
});