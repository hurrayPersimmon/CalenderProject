$(document).ready(function () {
    showLoginForm();


    if(sessionStorage.getItem('isLogin')!=='true') {
        console.log('로그아웃 상태');
        showLoginForm();
    } else {
        console.log('로그인 상태');
        showCalendar();
    }
    function showLoginForm() {
        $('#login-container').show();
        $('#calendar-container').show();
        $('#log-in').show();
        $('#log-out').hide();
        $('#deleteModal').modal('hide');
        $('#delete-button').hide();
        hideLoadingModal();
    }

    function showCalendar() {
        $('#login-container').hide();
        $('#signup-button').hide();
        $('#calendar-container').show();
        $('#log-in').hide();
        $('#log-out').show();
        $('#delete-button').show();
        $('.navbar-brand').text( sessionStorage.getItem('username') + "'s Calendar"); // 네비게이션 바의 Calendar App 부분 변경
        loadUserEvents(sessionStorage.getItem('username'));
    }

    // window.signup = function () {
    //     openModal();
    // };
});

const hostName = window.location.hostname;
console.log(hostName);
const hostNameServerUrl = 'http://' + hostName + ':8080';

// calendar element 취득
let calendarEl = $('#calendar')[0];
// full-calendar 생성하기
let calendar = new FullCalendar.Calendar(calendarEl, {
    height: '700px', // calendar 높이 설정
    expandRows: true, // 화면에 맞게 높이 재설정
    slotMinTime: '08:00', // Day 캘린더에서 시작 시간
    slotMaxTime: '20:00', // Day 캘린더에서 종료 시간
    // 해더에 표시할 툴바
    headerToolbar: {
        left: 'prev,next today',
        center: 'title',
        right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
    },
    initialView: 'dayGridMonth', // 초기 로드 될때 보이는 캘린더 화면(기본 설정: 달)
    initialDate: undefined, // 초기 로드 될때 보이는 날짜(기본 설정: 오늘)
    navLinks: JSON.parse(sessionStorage.getItem('isLogin')), // 날짜를 선택하면 Day 캘린더나 Week 캘린더로 링크
    editable: false, // 수정 가능?
    selectable: JSON.parse(sessionStorage.getItem('isLogin')), // 달력 일자 드래그 설정가능
    nowIndicator: true, // 현재 시간 마크
    dayMaxEvents: true, // 이벤트가 오버되면 높이 제한 (+ 몇 개식으로 표현)
    locale: 'ko', // 한국어 설정
    eventAdd: function (obj) { // 이벤트가 추가되면 발생하는 이벤트
        console.log(obj);
    },
    eventChange: function (info) { // 이벤트가 수정되면 발생하는 이벤트
        const event = info.event;
        console.log(event.id);

        let sendData = {
            title: event.title,
            startTime: event.start.toISOString(),
            endTime: event.end.toISOString(),
            username: sessionStorage.getItem('username'),
            allDay: event.allDay,
        }

        $.ajax({
            url: hostNameServerUrl+'/api/event/update/'+ event.id,
            method: 'PATCH',
            contentType: 'application/json',
            data: JSON.stringify(sendData),
            success: function (response) {
                console.log('Event updating successfully:', response);
                // 서버로부터 응답을 받은 후, 수정 사항 변경
                calendar.updateEvent(event);

            },
            error: function (error) {
                console.error('Error updating event:', error);
            }
        });
    },
    eventRemove: function (obj) { // 이벤트가 삭제되면 발생하는 이벤트
        console.log(obj);
    },
    eventClick: function (info) { // 이벤트를 클릭했을 때 발생하는 이벤트
        // 클릭한 이벤트의 정보를 Modal에 표시
        const event = info.event;
        openEventModal(event);
        // Modal에는 수정, 삭제 기능이 있음.

    },

    select: function (arg) { // 캘린더에서 드래그로 이벤트를 생성할 수 있다.
        let title = prompt('Event Title:');
        if (title) {
            let newEvent = {
                title: title,
                start: arg.start,
                end: arg.end,
                allDay: arg.allDay,
                editable: true,
            };

            let sendData = {
                title: newEvent.title,
                startTime: newEvent.start.toISOString(),
                endTime: newEvent.end.toISOString(),
                username: sessionStorage.getItem('username'),
                allDay: newEvent.allDay,
            }

            $.ajax({
                url: hostNameServerUrl+'/api/event/save/'+sessionStorage.getItem('id'),
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(sendData),
                success: function (response) {
                    console.log('Event created successfully:', response);
                    // 서버로부터 응답을 받은 후, 캘린더에 이벤트 추가
                    const addedEvent = calendar.addEvent(newEvent);
                    addedEvent.setProp('id', response.id);
                },
                error: function (error) {
                    console.error('Error creating event:', error);
                }
            });

            calendar.unselect();
        }
    },
    // 하드 코딩 된 이벤트
    events: [
        {
            title: 'Minseo Birthday',
            start: '2023-11-07',
        },
        {
            title: 'Long Event',
            start: '2021-07-07',
            end: '2021-07-10'
        }
    ]
});
// 캘린더 랜더링
calendar.render();



//로그인 함수
window.login = function () {
    showLoadingModal();
    let username = $('#username').val();
    let password = $('#password').val();
    $.ajax({
        url: hostNameServerUrl+'/api/member/login',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({username: username, password: password}),
        success: function (data) {
            console.log('Login successful:', data);
            // 로그인 성공 시 캘린더 표시 및 환영 메시지 표시
            sessionStorage.setItem('isLogin', 'true');
            sessionStorage.setItem('username', data.username);
            sessionStorage.setItem('id', data.id);
            hideLoadingModal();
            window.location.reload();
        },
        error: function (error) {
            hideLoadingModal();
            console.error('Login failed:', error);
            alert('로그인에 실패했습니다. 다시 시도하세요.');
        }
    });
};

//로그아웃 함수
window.logout = function () {
    console.log('로그아웃');
    sessionStorage.setItem('isLogin', 'false');
    sessionStorage.removeItem('username');
    window.location.reload();
}

// 회원 탈퇴 요청시 모달로 회원 탈퇴 시 기존 데이터가 다 삭제됨을 알림
window.openModal = function () {
    $('#deleteModal').modal({
        backdrop: 'static',
        keyboard: false
    });
};


// 회원 탈퇴 함수
window.deleteMember = function () {
    let username = sessionStorage.getItem('username');
    let password = $('#deletePassword').val();
    let id = sessionStorage.getItem('id');
    console.log(username, password);
    $.ajax({
        url: hostNameServerUrl+'/api/member/delete',
        method: 'DELETE',
        contentType: 'application/json',
        data: JSON.stringify({id : id, username: username, password: password}),
        success: function (data) {
            console.log('Delete successful:', data);
            // 로그인 성공 시 캘린더 표시 및 환영 메시지 표시
            sessionStorage.setItem('isLogin', 'false');
            sessionStorage.removeItem('username');
            sessionStorage.removeItem('id');
            window.location.reload();
        },
        error: function (error) {
            console.error('Delete failed:', error);
            alert('회원 탈퇴에 실패했습니다. 다시 시도하세요.');
        }
    });
};

// 로딩 중인 경우 모달 보이기
function showLoadingModal() {
    $('#loadingModal').modal({
        backdrop: 'static',
        keyboard: false
    });
}

// 로딩 중인 경우 모달 숨기기
function hideLoadingModal() {
    $('#loadingModal').modal('hide');
}

// 서버로부터 memberId에 해당하는 이벤트를 가져와서 캘린더에 추가
function loadUserEvents(username) {
    $.ajax({
        url: hostNameServerUrl+'/api/event/get/'+sessionStorage.getItem('id'),
        method: 'GET',
        contentType: 'application/json',
        success: function (response) {
            if(response.length===0) {
                alert('일정이 없습니다.');
            } else {
                console.log('Events found:', response);
                // 서버로부터 응답을 받은 후, 캘린더에 이벤트 추가
                response.forEach(function (event) {
                    let data = {
                        title: event.title,
                        start: event.startTime,
                        end: event.endTime,
                        allDay: event.allDay,
                        editable: true,
                    }
                    calendar.addEvent(data);
                });

            }
        },
        error: function (error) {
            console.error('Error getting events', error);
        }
    });
}

function openEventModal(event) {
    console.log(event.extendedProps);
    console.log(event._instance.instanceId);
    console.log(event.endTime);
    if(event.endTime === undefined){
        $('#eventDeleteButton').hide();
        $('#eventUpdateButton').hide();
    }

    $('#eventModal').modal({
        backdrop: 'static',
        keyboard: false
    });


    document.getElementById('eventTitle').innerText = event.title;
    document.getElementById('eventStart').innerText = event.start.toLocaleString();
    document.getElementById('eventEnd').innerText = event.end ? event.end.toLocaleString() : '';
    document.getElementById('eventAllDay').innerText = event.allDay ? '⭕️' : '❌';

}

// 이벤트 추가


// 이벤트 수정

// 이벤트 삭제


