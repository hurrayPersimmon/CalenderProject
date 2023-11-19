$(document).ready(function () {
    showLoginForm();

    function showLoginForm() {
        $('#login-container').show();
        $('#calendar-container').show();
        $('#welcome-message').hide();
    }

    window.signup = function () {
        openModal();
    };


    $('#signup-form').submit(function (event) {
        event.preventDefault();
        // TODO: 회원가입 처리 로직 추가
        alert('Signup logic should be implemented here.');
        closeModal();
    });
});

// 모달 열기 함수
function openModal() {
    $('#signup-modal').show();
}

// 모달 닫기 함수
function closeModal() {
    $('#signup-modal').hide();
}

function showCalendar() {
    $('#login-container').hide();
    $('#calendar-container').show();
    $('#welcome-message').text('좋은 하루입니다, ' + $('#username').val() + '님');

    loadUserEvents(username);
}

window.login = function () {
    var username = $('#username').val();
    var password = $('#password').val();

    $.ajax({
        url: '/api/auth/login',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({username: username, password: password}),
        success: function (response) {
            // 로그인 성공 시 캘린더 표시 및 환영 메시지 표시
            showCalendar(username);
        },
        error: function (error) {
            console.error('Login failed:', error);
            alert('로그인에 실패했습니다. 다시 시도하세요.');
        }
    });
};

function loadUserEvents(username) {
    $.ajax({
        url: '/api/events',
        method: 'GET',
        contentType: 'application/json',
        data: {username: username},
        success: function (response) {
            console.log('Events found:', response);
            // 서버로부터 응답을 받은 후, 캘린더에 이벤트 추가
            response.forEach(function (event) {
                calendar.addEvent(event);
            });
        },
        error: function (error) {
            console.error('Error getting events', error);
        }
    });
}

// calendar element 취득
var calendarEl = $('#calendar')[0];
// full-calendar 생성하기
var calendar = new FullCalendar.Calendar(calendarEl, {
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
    initialDate: undefined,
    navLinks: true, // 날짜를 선택하면 Day 캘린더나 Week 캘린더로 링크
    editable: true, // 수정 가능?
    selectable: true, // 달력 일자 드래그 설정가능
    nowIndicator: true, // 현재 시간 마크
    dayMaxEvents: true, // 이벤트가 오버되면 높이 제한 (+ 몇 개식으로 표현)
    locale: 'ko', // 한국어 설정
    eventAdd: function (obj) { // 이벤트가 추가되면 발생하는 이벤트
        console.log(obj);
    },
    eventChange: function (obj) { // 이벤트가 수정되면 발생하는 이벤트
        console.log(obj);
    },
    eventRemove: function (obj) { // 이벤트가 삭제되면 발생하는 이벤트
        console.log(obj);
    },

    select: function (arg) { // 캘린더에서 드래그로 이벤트를 생성할 수 있다.
        var title = prompt('Event Title:');
        if (title) {
            var newEvent = {
                title: title,
                start: arg.start,
                end: arg.end,
                allDay: arg.allDay

            };

            $.ajax({
                url: '/api/createEvent',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(newEvent),
                success: function (response) {
                    console.log('Event created successfully:', response);
                    // 서버로부터 응답을 받은 후, 캘린더에 이벤트 추가
                    calendar.addEvent(newEvent);
                },
                error: function (error) {
                    console.error('Error creating event:', error);
                }
            });
        }
        calendar.unselect();
    },
    // 이벤트
    events: [
        {
            title: 'Minseo Birthday',
            start: '2023-11-07',
        },
        {
            title: 'Long Event',
            start: '2021-07-07',
            end: '2021-07-10'
        },
        {
            groupId: 999,
            title: 'Repeating Event',
            start: '2021-07-09T16:00:00'
        },
        {
            groupId: 999,
            title: 'Repeating Event',
            start: '2021-07-16T16:00:00'
        },
        {
            title: 'Conference',
            start: '2021-07-11',
            end: '2021-07-13'
        },
        {
            title: 'Meeting',
            start: '2021-07-12T10:30:00',
            end: '2021-07-12T12:30:00'
        },
        {
            title: 'Lunch',
            start: '2021-07-12T12:00:00'
        },
        {
            title: 'Meeting',
            start: '2021-07-12T14:30:00'
        },
        {
            title: 'Happy Hour',
            start: '2021-07-12T17:30:00'
        },
        {
            title: 'Dinner',
            start: '2021-07-12T20:00:00'
        },
        {
            title: 'Birthday Party',
            start: '2021-07-13T07:00:00'
        },
        {
            title: 'Click for Google',
            url: 'http://google.com/', // 클릭시 해당 url로 이동
            start: '2021-07-28'
        }
    ]
});
// 캘린더 랜더링
calendar.render();
