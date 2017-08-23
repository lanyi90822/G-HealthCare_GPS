package com.example.hp.stepcount.Http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by bin on 10/02/2017.
 */
public class MagicClientImpl implements MagicClient {

    private final String tag = "MagicClientImp";
  //  private Logger log = LoggerFactory.getLogger(MagicClientImpl.class);

    private final ExecutorService executorService = Executors
            .newCachedThreadPool();
//   private EvnCallback evnCallback;

    private String host;

    private String sessionID;

    @Override
    public void addTask(Task task) {
   //     if (log != null) {
  //          log.debug(tag, task.toString());
 //       }
        task.setMagicServer(this);
        if (task instanceof HttpTask) {
//            if (evnCallback != null && !evnCallback.isNetworkConnected()) {
//                HttpTask httpTask = (HttpTask) task;
//                OnTaskFinishedListener onTaskFinishedListener = httpTask
//                        .getOnTaskFinishedListener();
//                if (onTaskFinishedListener != null) {
//                    onTaskFinishedListener.onTaskFinished(-1, null, httpTask);
//                }
//                return;
//            }
        }
        try {
            executorService.execute(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSessionID(String sessionId) {
        this.sessionID = sessionId;
    }

    @Override
    public String getSessionID() {
        return sessionID;
    }
}
