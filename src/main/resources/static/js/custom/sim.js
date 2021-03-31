//自定义方法
(function ($) {
    $.fn.serializeJson = function () {
        var serializeObj = {};
        var array = this.serializeArray();
        var str = this.serialize();
        $(array).each(function () {
            if (serializeObj[this.name]) {
                if ($.isArray(serializeObj[this.name])) {
                    serializeObj[this.name].push(this.value);
                } else {
                    serializeObj[this.name] = [serializeObj[this.name], this.value];
                }
            } else {
                serializeObj[this.name] = this.value;
            }
        });
        return serializeObj;
    };
})(jQuery);

//核心方法
$(function () {
    loadProduct();
    var serverUrl;

    $("#serverUrl").change(function () {
        serverUrl = $("#serverUrl").val();
        loadProduct();
    });


    $("#product-select").change(function () {

        $("#deviceCode").val("");
        $("#deviceSecret").val("");
        $("#productKey").val($("#product-select").find("option:selected").attr("productKey"));

        var productId = $(this).val()
        serverUrl = $("#serverUrl").val();
        $.ajax({
            url: "./api/getData",
            data: {"productId": productId, "serverUrl": serverUrl, "step": "devices"},
            type: "POST",
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    var data = result.data;
                    if (data.list) {
                        var list = data.list;
                        var data = result.data;
                        var deviceStr = "<option selected>Pls select device</option>";
                        for (var i = 0; i < list.length; i++) {
                            var device = list[i];
                            deviceStr += "<option  data-icon='glyphicon glyphicon-heart' data-tokens='" + i + "' value='" + device.deviceCode + "'> " + device.deviceName + "(" + device.id + ")" + "</option>";
                        }
                        $("#devices-select").html(deviceStr);
                    }
                } else {
                    alert(responseData.message);
                }
            }
        });
    });

    $("#devices-select").change(function () {
        var deviceCode = $("#devices-select").find("option:selected").val();
        var productId = $("#product-select").find("option:selected").val();
        $("#deviceCode").val(deviceCode);
        serverUrl = $("#serverUrl").val();
        $.ajax({
            url: "./api/getData",
            data: {"productId": productId, "deviceCode": deviceCode, "serverUrl": serverUrl, "step": "single"},
            type: "POST",
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    $("#deviceSecret").val(result.data.deviceSecret);
                } else {
                    alert(responseData.message);
                }
            }
        });

        var productKey = $("#product-select").find("option:selected").attr("productKey");
        //加载Topic
        loadTopics(serverUrl, productId, productKey, deviceCode)
    });
    $("#getArribute").click(function () {
        var deviceCode = $("#deviceCode").val();
        var deviceSecret = $("#deviceSecret").val();
        if (deviceCode == "" || deviceSecret == "") {
            alert('Pls select device and device ');
            return false;
        }
        $.ajax({
            url: "./api/getData",
            data: {
                "deviceCode": deviceCode,
                "deviceSecret": deviceSecret,
                "serverUrl": serverUrl,
                "step": "thingModel"
            },
            type: "POST",
            dataType: "json",
            success: function (responseData) {
                if (responseData.success) {
                    var data = responseData.data;
                    if (data.properties) {
                        var properties = data.properties;

                        var tree = {};

                        for (var i = 0; i < properties.length; i++) {
                            var property = properties[i];
                            if (!property.parentId) {
                                if (!tree["0"]) {
                                    tree["0"] = [];
                                }
                                tree["0"].push(property);
                            } else {
                                if (!tree[property.parentId]) {
                                    tree[property.parentId] = [];
                                }
                                tree[property.parentId].push(property);
                            }
                        }

                        var html = [];
                        console.log("tree=" + tree);
                        generateRows("0", tree, html);
                        $("#tableBody").html(html.join(""));

                        $("#submit").removeAttr("disabled");
                    }

                } else {
                    alert(responseData.message);
                }
            }
        });


    });

    $("#submit").click(function () {
        var formData = $("#dataForm").serializeJson();
        var deviceCode = $("#deviceCode").val();
        var deviceSecret = $("#deviceSecret").val();
        var arrayFields = $("#arrayFields").val();
        var productKey = $("#product-select").find("option:selected").attr("productKey");

        $.ajax({
            url: "./api/publish",
            data: JSON.stringify({
                deviceCode: deviceCode,
                arrayFields: arrayFields,
                productKey: productKey,
                deviceSecret: deviceSecret,
                serverUrl: serverUrl,
                data: formData
            }),
            type: "POST",
            contentType: "application/json;charset=utf-8",
            dataType: "json",
            success: function (response) {
                alert("success!");
            }
        })
    });

    $("#getData").click(function () {
        var serverUrl = $("#serverUrl").val();
        const apiUrl = $("#apiUrl").val();
        if (apiUrl == null || apiUrl == '') {
            $(apiUrl).focus();
            alert("API URL CAN'T BE NULL!");
            return;
        }
        $.ajax({
            url: "./api/getData",
            data: {
                "apiUrl": apiUrl,
                "serverUrl": serverUrl,
                "step": "custom_api"
            },
            type: "POST",
            dataType: "json",
            success: function (responseData) {
                if (responseData.success) {
                    var data = responseData.data;
                    $("#payload").val(JSON.stringify(data, null, 4));
                } else {
                    alert(responseData.message);
                }
            }
        });
    });
    //for MQ send
    $("#sendMq").click(function () {
        var serverUrl = $("#serverUrl").val();

        /* const apiUrl = $("#apiUrl").val();
         if (apiUrl == null || apiUrl == '') {
             $(apiUrl).focus();
             alert("API URL CAN'T BE NULL!");
             return;
         }*/

        const payload = $("#payload").val();
        if (payload == null || payload == '') {
            $(payload).focus();
            alert("PAYLOAD CAN'T BE NULL!");
            return;
        }
        var topic = $("#topics-select").find("option:selected").val();
        if (topic == "Pls select topics") {
            alert("TOPIC CAN'T BE NULL!");
            return;
        }
        var deviceCode = $("#deviceCode").val();
        var deviceSecret = $("#deviceSecret").val();
        var productKey = $("#product-select").find("option:selected").attr("productKey");

        $.ajax({
            url: "./api/publish",
            data: JSON.stringify({
                serverUrl: serverUrl,
                productKey: productKey,
                deviceCode: deviceCode,
                deviceSecret: deviceSecret,
                payload: payload,
                topic: topic
            }),
            type: "POST",
            contentType: "application/json;charset=utf-8",
            dataType: "json",
            success: function (response) {
                alert("success!");
            }
        });
    });

    //payload
    $("#payload").blur(function () {
        const content = $("#payload").val();
        const data = JSON.parse(content);
        $("#payload").val(JSON.stringify(data, null, 4));
    });

});

//====================================
/**
 * 加载产品
 */
function loadProduct() {
    var serverUrl = $("#serverUrl").val();
    if (serverUrl) {
        //fill product
        $.ajax({
            url: "./api/getData",
            data: {"serverUrl": serverUrl, "step": "product"},
            type: "POST",
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    var data = result.data;
                    if (data.list) {
                        var list = data.list;
                        var data = result.data;
                        var productStr = "<option selected>Pls select product</option>";
                        for (var i = 0; i < list.length; i++) {
                            var product = list[i];
                            productStr += "<option  data-icon='glyphicon glyphicon-heart' productKey='" + product.productKey + "' value='" + product.id + "'> " + product.productName + "-(" + product.id + ")-" + "-(" + product.nodeType + ")-" + "-(" + product.status +  ")-(" + product.deviceCount + ")" + "</option>";
                        }
                        $("#product-select").html(productStr);
                    }
                } else {
                    alert(responseData.message);
                }
            }
        });
    } else {
        var productStr = "<option selected>Pls select product</option>";
        $("#product-select").html(productStr);
    }

}

/**
 * 截止Topic
 * @param serverUrl
 * @param productId
 * @param productKey
 * @param deviceCode
 */
function loadTopics(serverUrl, productId, productKey, deviceCode) {
    if (serverUrl) {
        $.ajax({
            url: "./api/getData",
            data: {
                "serverUrl": serverUrl,
                "productId": productId,
                "productKey": productKey,
                "deviceCode": deviceCode,
                "step": "topics"
            },
            type: "POST",
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    var data = result.data;
                    var topicStr = "<option selected>Pls select topics</option>";
                    for (var i = 0; i < data.length; i++) {
                        var topic = data[i];
                        topicStr += "<option  data-icon='glyphicon glyphicon-heart'  topicKey='" + topic.topicKey + "' value='" + topic.topicPath + "'> " + "(" + topic.category + ")->" + topic.topicPath + "</option>";
                    }
                    $("#topics-select").html(topicStr);

                } else {
                    alert(responseData.message);
                }
            }
        });
    } else {
        var productStr = "<option value='-1' selected>Pls select topics</option>";
        $("#topics-select").html(productStr);
    }
}

/**
 * 创建Rom
 * @param id
 * @param treedata
 * @param html
 */
function generateRows(id, treedata, html) {
    var children = treedata[id];
    for (var i = 0; i < children.length; i++) {
        html.push("<tr>");
        var style = '';
        if (children[i].dataType == 'JSON') {
            style = " style='font-weight:bold' ";
        }
        var blank = "";
        if (children[i].treeCode) {
            for (var j = 0; j < children[i].treeCode.split(".").length - 1; j++) {
                blank += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            }
        }
        var unit = "";
        if (children[i].spec && children[i].spec.unit) {
            unit = children[i].spec.unit;
        }

        var placeHolder = "";
        if (children[i].spec) {
            if (children[i].spec.min) {
                placeHolder += "min:" + children[i].spec.min + ";"
            }
            if (children[i].spec.max) {
                placeHolder += "max:" + children[i].spec.max + ";"
            }
            if (children[i].spec.step) {
                placeHolder += "step:" + children[i].spec.step + ";"
            }
            if (children[i].spec.formatString) {
                placeHolder += "formatString:" + children[i].spec.formatString + ";"
            }
            if (children[i].spec.dataType) {
                placeHolder += "dataType:" + children[i].spec.dataType + ";"
            }
        }

        var inputForm = "";
        //类型参考com.lighting.dm.form.model.protocol.DataType
        //number type
        var numberType = ["INT32", "DOUBLE", "FLOAT", "BOOLEAN", "BIG_DECIMAL"];

        //text type
        var textType = ["TEXT", "DATE", "BITS"];

        var arrayFields = $("#arrayFields").val();

        if (children[i].dataType != "JSON") {
            if (numberType.indexOf(children[i].dataType) >= 0) {
                inputForm = ' <input class="form-control" name="' + children[i].path + '" type="number" placeholder="' + placeHolder + '" >';
            } else if (textType.indexOf(children[i].dataType) >= 0) {
                inputForm = ' <input class="form-control" name="' + children[i].path + '" type="text"  placeholder="' + placeHolder + '" >';
            } else if (children[i].dataType == "ENUM") {
                var optionStr = '<option value="">Pls Check </option>';
                $.each(children[i].spec.keyValues, function (k, v) {
                    optionStr += '<option value="' + k + '">' + k + '</option>';
                    console.log("k=" + k + ",v=" + v);
                });
                inputForm = '<select class="form-control" name="' + children[i].path + '">' + optionStr + '</select>';
            } else if (children[i].dataType == "ARRAY") {
                arrayFields += children[i].path + ",";
                $("#arrayFields").val(arrayFields);
                inputForm = ' <input class="form-control" name="' + children[i].path + '" type="text"  placeholder="' + placeHolder + '" >English comma separated, do not add brackets(just for Array)';
            }
        }

        html.push("<td " + style + ">" + blank + children[i].name + "</td>");
        html.push("<td>" + inputForm + "</td>");
        html.push("<td>" + unit + "</td>");
        html.push("</tr>");

        if (treedata[children[i].id]) {
            html.push(generateRows(children[i].id, treedata, html))
        }
    }
}