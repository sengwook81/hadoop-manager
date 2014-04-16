/**
 * 
 */
Ext.define('Zero.util.cmp.ComboboxColumn',{
	extend:'Ext.grid.column.Column',
	alias:'widget.zcombobox',
	editable:false,
	store:null,
	namefield:'name',
	codefield:'code',
	initComponent:function () {
		var me = this;
		Ext.apply(me,{
			renderer: function (val) {
				if(me.store != null) {
					var index = me.store.findExact(me.codefield,val);
					if(index >= 0) {
						if (typeof(me.namefield) == "function") {
							return me.namefield(me.store.getAt(index));
						}
						else {
							return me.store.getAt(index).get(me.namefield);
						}
					}	
				}
			}
			
		});
		
		if(me.editable) {
			Ext.apply(me,{
				editor: {
			        xtype: 'combobox',
			        store: me.store,
			        queryMode: 'local',
			        displayField: me.namefield,
			        valueField: me.codefield
			    }
			});
		}

		me.callParent(arguments);
	}

});