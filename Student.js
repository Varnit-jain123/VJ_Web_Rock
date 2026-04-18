const base = "schoolService/studentTable";

// ADD
function add() {
    $.ajax({
        url: base + "/add",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            rollNumber: $("#r1").val(),
            name: $("#n1").val(),
            gender: $("#g1").val()
        }),
        success: function (data) {
            $("#output").text(data);
        }
    });
}

// UPDATE
function update() {
    $.ajax({
        url: base + "/update",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            rollNumber: $("#r2").val(),
            name: $("#n2").val(),
            gender: $("#g2").val()
        }),
        success: function (data) {
            $("#output").text(data);
        }
    });
}

// DELETE
function del() {
    $.ajax({
        url: base + "/delete",
        type: "GET",
        data: { rollNumber: $("#r3").val() },
        success: function (data) {
            $("#output").text(data);
        }
    });
}

// GET BY
function getBy() {
    $.ajax({
        url: base + "/getBy",
        type: "GET",
        dataType: "json",
        data: { rollNumber: $("#r4").val() },
        success: function (s) {

            let html = `
                <table border="1">
                    <tr>
                        <th>Roll</th>
                        <th>Name</th>
                        <th>Gender</th>
                    </tr>
                    <tr>
                        <td>${s.rollNumber}</td>
                        <td>${s.name}</td>
                        <td>${s.gender}</td>
                    </tr>
                </table>
            `;

            $("#output").html(html);
        }
    });
}

// GET ALL (TABLE VIEW)
function getAll() {
    $.ajax({
        url: base + "/getAll",
        type: "GET",
        dataType: "json",
        success: function (data) {

            let html = `
                <table border="1" style="width:100%; text-align:center;">
                    <tr>
                        <th>Roll</th>
                        <th>Name</th>
                        <th>Gender</th>
                    </tr>
            `;

            data.forEach(s => {
                html += `
                    <tr>
                        <td>${s.rollNumber}</td>
                        <td>${s.name}</td>
                        <td>${s.gender}</td>
                    </tr>
                `;
            });

            html += "</table>";

            $("#output").html(html);
        }
    });
}