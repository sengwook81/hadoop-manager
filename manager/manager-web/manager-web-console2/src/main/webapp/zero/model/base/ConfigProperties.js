/**
 * 
 */
Ext.define('Zero.model.base.ConfigProperties', {
	extend : 'Ext.data.Model',
	fields : [ 'del_Yn','app_Id', 'config_Id', 'prop_Key', 'prop_Val', 'prop_Desc' , 'prop_Type'],
	validations: 
    [
          {type: 'presence',  field: 'app_Id'},
          {type: 'presence',  field: 'config_Id'},
          {type: 'presence',  field: 'prop_Key'}
     ]
});