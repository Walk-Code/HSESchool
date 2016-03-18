package com.model;

public interface TaskListener {

	public void taskStarted(TaskType type);
	public void taskFinished(TaskType type, Object result, boolean isHistory);
	public void taskIsCanceled(TaskType type);
}