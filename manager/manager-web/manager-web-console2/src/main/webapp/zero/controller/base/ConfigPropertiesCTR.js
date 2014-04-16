/**
 * 
 */
Ext.define('Zero.controller.base.ConfigPropertiesCTR',{
	extend: 'Ext.app.Controller',
	stores: [
	         'base.ConfigProperties'
	],
	refs: [{
        ref: 'gridConfigProperty',
        selector: '#gridConfigProperty'
    },{
        ref: 'baseConfigProperties',
        selector: '#baseConfigProperties'
    }
	
	],
	views: ['base.ConfigProperties'],
	init:function () {
		console.log("Load ConfigPropertiesCTR",arguments);
		var me = this;
		me.control({
			'#gridConfigProperty':{
				edit:function(editor, obj) {
	                //check if record is dirty 
	                if(obj.record.dirty){
	                    //check if the record is valid   
	                    console.log("Validate Check " , obj.record,obj);
	                    if(!obj.record.validate().isValid()){
	                    	var validateObj = obj.record.validate();
	                    	Ext.MessageBox.show({
	            				title : "Validation Error",
	            				msg : validateObj.items[0].field + " " + validateObj.items[0].message,
	            				icon : Ext.MessageBox.OK,
	            				buttons : Ext.Msg.OK
	            			});
	                    }
	                }
	            }
			},
			'baseConfigProperties':{
				afterrender:function () {
					console.log("After Render Property Window",me,me.getBaseConfigProperties());
					
					me.getBaseConfigPropertiesStore().load({
						params:me.getBaseConfigProperties().param
					});
					
					me.getGridConfigProperty().suspendEvents();
					me.getGridConfigProperty().reconfigure(me.getBaseConfigPropertiesStore());
					me.getGridConfigProperty().resumeEvents();
				}
			},'baseConfigProperties button[text=REFRESH]': {
				click:function () {
					me.getBaseConfigPropertiesStore().load({params:me.getBaseConfigProperties().param});
				}
			},
			'baseConfigProperties button[text=ADD]':{
				click:function (el) {
					console.log("Click Add Row");
					me.getBaseConfigPropertiesStore().add(me.getBaseConfigProperties().param);
				}
			},
			'baseConfigProperties button[text=SAVE]':{
				click:function (el) {
					console.log("Click Save Row");
					me.getBaseConfigPropertiesStore().sync({
						callback:function (){
							alert("Reload Data");
							console.log("me.getBaseConfigProperties().param",me.getBaseConfigProperties().param);
							me.getBaseConfigPropertiesStore().load({params:me.getBaseConfigProperties().param});
						},
						failure:function() {
							console.log("Sync Fail" , arguments);
							alert("Fail");
						}
					},me);
					
				}
			}
		});
		me.callParent();
	}
});