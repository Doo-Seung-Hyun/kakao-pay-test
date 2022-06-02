-- 상담사(user) 테스트 데이터 생성
-- 패스워드 asdf1234!@
insert into user(user_email_addr, password, user_nm, created_datetime) values('lpy321@daum.net', '$2a$10$wpqVGFEQtutc.NWbE61QPukb/mXT0ZLB7iBiEB98auDbIFjA6We4C', '이페이', parsedatetime('20210602104224','yyyymmddhhmmss'));
insert into user(user_email_addr, password, user_nm, created_datetime) values('applyguy33@daum.net', '$2a$10$wpqVGFEQtutc.NWbE61QPukb/mXT0ZLB7iBiEB98auDbIFjA6We4C', '지원자', parsedatetime('20211213093255','yyyymmddhhmmss'));
insert into user(user_email_addr, password, user_nm, created_datetime) values('qwerqwer17@daum.net', '$2a$10$wpqVGFEQtutc.NWbE61QPukb/mXT0ZLB7iBiEB98auDbIFjA6We4C', '두승현', parsedatetime('20220313103115','yyyymmddhhmmss'));
insert into user(user_email_addr, password, user_nm, created_datetime) values('kkkim@naver.com', '$2a$10$wpqVGFEQtutc.NWbE61QPukb/mXT0ZLB7iBiEB98auDbIFjA6We4C', '테스트', parsedatetime('20210501081155','yyyymmddhhmmss'));

-- 질문글(board) 테스트 데이터 생성
insert into inquiry(content, created_datetime, title, customer_id)
values(
          '구매하고첫개시했는데 집에오니 단추 밑에 두개가 없어쟜어요......\n단추 구매가능한가요ㅠㅜ',
          parsedatetime('2022-05-26 01:14:32','yyyy-mm-dd hh:mm:ss'),
          '상품문의',
          'kakaokim321'
      );

insert into inquiry(content, created_datetime, title, customer_id)
values(
          '주문했는데 사이즈 1 에서 2 로 변경 부탁 드려요~',
          parsedatetime('2022-05-11 01:53:25','yyyy-mm-dd hh:mm:ss'),
          '사이즈문의',
          'ssura123'
      );

insert into inquiry(content, created_datetime, title, customer_id)
values(
          '안녕하세요! 제가 주문한게 3차인지 4차인지 헷갈리는데, 혹시 알수있을까요? 6월 10 일전에는 입고됐음 좋겠는데 가능할까요? 확인부탁드리겠습니다!',
          parsedatetime('2022-05-10 09:52:07','yyyy-mm-dd hh:mm:ss'),
          '배송문의',
          'llnayo'
      );

insert into inquiry(content, created_datetime, title, customer_id)
values(
          '지금 1사이즈 주문하면 배송이 4차인가요? 3차는 끝났나요?',
          parsedatetime('2022-05-09 15:37:35','yyyy-mm-dd hh:mm:ss'),
          '상품문의',
          'leejin9787'
      );

insert into inquiry(content, created_datetime, title, customer_id)
values(
          '얘기엄마인데요 평소 55사이즈. 입거든요. 바지 27사지즈 입는데요 원피스 몇사이즈 선택할지 고민이네요',
          parsedatetime('2022-05-03 19:01:39','yyyy-mm-dd hh:mm:ss'),
          '사이즈문의',
          'zhz120123'
      );

insert into inquiry(content, created_datetime, title, customer_id)
values(
          '방금 주문했는데 3차에 오나요?\n2차 취소분 발생했을 경우 2차 기간 안에 받을 가능성이 있는지 문의드립니다^^',
          parsedatetime('2022-05-03 12:47:55','yyyy-mm-dd hh:mm:ss'),
          '상품문의',
          'choimi44'
      );

insert into inquiry(content, created_datetime, title, customer_id)
values(
          '신축성 어떤가요?\n청바지 힙 49입는데 1사이즈 입어도 괜찮을까요?',
          parsedatetime('2022-04-27 22:15:26','yyyy-mm-dd hh:mm:ss'),
          '상품문의',
          'rhthdl55'
      );

insert into inquiry(content, created_datetime, title, customer_id)
values(
          '사이즈1 재입고 언제되나요?',
          parsedatetime('2022-04-25 00:44:38','yyyy-mm-dd hh:mm:ss'),
          '상품문의',
          'onlyindre'
      );

insert into inquiry(content, created_datetime, title, customer_id)
values(
          '2사이즈 재입고 일정 있나요?',
          parsedatetime('2022-04-23 21:34:44','yyyy-mm-dd hh:mm:ss'),
          '재입고문의',
          'am121333'
      );

insert into inquiry(content, created_datetime, title, customer_id)
values(
          '원피스 안감있나요? 평소 바지만 입었던지라 허리27 정사이즈를 입는데요 1사이즈? 2사이즈 중 어떤게 나을까요',
          parsedatetime('2022-04-23 15:00:39','yyyy-mm-dd hh:mm:ss'),
          '상품문의',
          'mullan499'
      );

insert into inquiry(content, created_datetime, title, customer_id)
values(
          '1사이즈 언제입고되나요?',
          parsedatetime('2022-04-23 04:39:11','yyyy-mm-dd hh:mm:ss'),
          '상품문의',
          't20210qwe'
      );

insert into answer(inquiry_id, user_id, title, content, is_finished, accepted_datetime, finished_datetime)
values(1, 1, '답변드립니다', '네 가능합니다. 원하시는 경우 031-0311-1111로 연락주시면 상담 후 진행해드리겠습니다. 감사합니다.','Y'
, parsedatetime('2022-01-28 11:47:55','yyyy-mm-dd hh:mm:ss')
, parsedatetime('2022-01-28 12:35:15','yyyy-mm-dd hh:mm:ss'));

insert into answer(inquiry_id, user_id, title, content, is_finished, accepted_datetime, finished_datetime)
values(2, 2, '답변드립니다', '이미 보증기간이 지나 교체가 어렵습니다. 고객님의 너그러운 양해 부탁드립니다. 감사합니다.','Y'
, parsedatetime('2022-01-11 11:47:55','yyyy-mm-dd hh:mm:ss')
, parsedatetime('2022-01-11 12:11:55','yyyy-mm-dd hh:mm:ss'));

insert into answer(inquiry_id, user_id, title, content, is_finished, accepted_datetime, finished_datetime)
values(3, 1, '답변드립니다', '안녕하세요 고객님 먼저 기다리게 해드려 대단히 죄송합니다.  곧 배송될 예정이오니 조금만 기다려주세요. 감사합니다.','Y'
, parsedatetime('2022-01-10 11:55:55','yyyy-mm-dd hh:mm:ss')
, parsedatetime('2022-01-10 14:25:11','yyyy-mm-dd hh:mm:ss'));

insert into answer(inquiry_id, user_id, title, content, is_finished, accepted_datetime, finished_datetime)
values(4, 4, '답변드립니다', '안녕하세요 고객님, 4차입니다. 감사합니다.','Y'
, parsedatetime('2022-01-14 12:17:55','yyyy-mm-dd hh:mm:ss')
, parsedatetime('2022-01-14 13:57:13','yyyy-mm-dd hh:mm:ss'));

insert into answer(inquiry_id, user_id, title, content, is_finished, accepted_datetime, finished_datetime)
values(5, 4, '답변드립니다', '안녕하세요 고객님, 55사이즈를 추천드립니다. 감사합니다.','Y'
,parsedatetime('2022-01-04 21:27:55','yyyy-mm-dd hh:mm:ss')
,parsedatetime('2022-01-05 09:37:55','yyyy-mm-dd hh:mm:ss'));

insert into answer(inquiry_id, user_id, title, content, is_finished, accepted_datetime, finished_datetime)
values(6, 3, '답변드립니다', '안녕하세요 고객님, 고객님의 경우 3차에 배송될 것으로 예상됩니다. 감사합니다.','Y'
, parsedatetime('2022-01-04 11:47:55','yyyy-mm-dd hh:mm:ss')
, parsedatetime('2022-01-04 16:22:22','yyyy-mm-dd hh:mm:ss'));

insert into answer(inquiry_id, user_id, title, content, is_finished)
values(7, 3, null, null,'N');

insert into answer(inquiry_id, user_id, title, content, is_finished)
values(8, 1, null, null,'N');