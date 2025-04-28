function deleteProduct(endpoint, id) {
    if (confirm("Bạn chắc chắn xóa?") === true) {
        fetch(`${endpoint}/${id}`, {
            method: "delete"
        }).then(res => {
            if (res.status === 204) {
                alert("Xóa thành công!");
                location.reload();
            } else
                alert("Hệ thống bị lỗi!");
        });
    }
}
function deleteShop(endpoint, id) {
    if (confirm("Bạn chắc chắn muốn xóa cửa hàng này?") === true) {
        fetch(`${endpoint}/${id}`, {
            method: "DELETE",

        }).then(res => {
            if (res.status === 204) {
                alert("Xóa cửa hàng thành công!");
                location.reload();
            } else if (res.status === 401) {
                alert("Vui lòng đăng nhập lại!");
                window.location.href = "/SanThuongMai/login";
            } else {
                alert("Lỗi khi xóa cửa hàng!");
            }
        }).catch(err => {
            alert("Lỗi kết nối: " + err.message);
        });
    }
}
function deleteUser(url, id) {
    try {
        if (confirm("Bạn có chắc muốn xóa người dùng này?")) {
            fetch(`${url}/${id}`, {
                method: "DELETE"
            }).then(res => {
                if (res.ok) {
                    alert("Xóa người dùng thành công!");
                    location.reload();
                } else {
                    return res.json().then(errorData => {
                        console.error(res);
                        console.error(errorData);
                        alert(errorData.error);
                    });
                }
            });
        }
    } catch (err) {
        alert(err);
    }
   
}