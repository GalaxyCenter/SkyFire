package apollo.tianya.service;

interface INoticeService {
   void scheduleNotice();
   void requestNotice();
   void clearNotice(int type);
}
