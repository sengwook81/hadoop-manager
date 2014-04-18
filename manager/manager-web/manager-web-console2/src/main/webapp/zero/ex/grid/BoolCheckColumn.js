/**
 * Grid 내 CheckBox Delete용도의 컴포넌트.
 */
Ext.define('Zero.ex.grid.BoolCheckColumn', {
	extend : 'Ext.grid.column.CheckColumn',
	alias:'widget.boolcheckcolumn',
	stopSelection : false,
	initComponent:function () {
		var me = this;
		me.checkVal = this.checkVal !== undefined? this.checkVal : 'Y';
		me.uncheckVal = this.uncheckVal !== undefined? this.uncheckVal : 'N';
		me.callParent();
	},
	listeners : {
		checkchange : function(el, rowIndex, checked, opts) {
			var me = this;
			if(el['dataIndex']) {
				console.log("CheckedEvent Row : ", checked);
				record = el.up('grid').getStore().getAt(rowIndex);
				if(checked) {
					record.set(el['dataIndex'],me.checkVal);
				}
				else{ 
					record.set(el['dataIndex'],me.uncheckVal);
				}
			}
		}
	},
	// OVERRIDE
	renderer : function(value, meta) {
		var me = this;
        var cssPrefix = Ext.baseCSSPrefix,
            cls = [cssPrefix + 'grid-checkcolumn'];

        if (this.disabled) {
            meta.tdCls += ' ' + this.disabledCls;
        }
        // Value 
        console.log("BOOL CHECKBOX VAL" , value);
        if (value == me.checkVal) {
            cls.push(cssPrefix + 'grid-checkcolumn-checked');
        }
        return '<img class="' + cls.join(' ') + '" src="' + Ext.BLANK_IMAGE_URL + '"/>';
    },
    processEvent: function(type, view, cell, recordIndex, cellIndex, e, record, row) {
        var me = this,
            key = type === 'keydown' && e.getKey(),
            mousedown = type == 'mousedown';

        if (!me.disabled && (mousedown || (key == e.ENTER || key == e.SPACE))) {
            var dataIndex = me.dataIndex,
            	// PositiveValue Change
                checked = !(record.get(dataIndex) == me.checkVal);

            // Allow apps to hook beforecheckchange
            if (me.fireEvent('beforecheckchange', me, recordIndex, checked) !== false) {
                record.set(dataIndex, checked);
                me.fireEvent('checkchange', me, recordIndex, checked);

                // Mousedown on the now nonexistent cell causes the view to blur, so stop it continuing.
                if (mousedown) {
                    e.stopEvent();
                }

                // Selection will not proceed after this because of the DOM update caused by the record modification
                // Invoke the SelectionModel unless configured not to do so
                if (!me.stopSelection) {
                    view.selModel.selectByPosition({
                        row: recordIndex,
                        column: cellIndex
                    });
                }

                // Prevent the view from propagating the event to the selection model - we have done that job.
                return false;
            } else {
                // Prevent the view from propagating the event to the selection model if configured to do so.
                return !me.stopSelection;
            }
        } else {
            return me.callParent(arguments);
        }
    }
});