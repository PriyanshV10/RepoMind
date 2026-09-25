"use client";

import ChatView from "@/components/chat/chat-view";
import { RequireAuth } from "@/components/providers/require-auth";
import { use } from "react";

export default function ChatPage({
  params,
}: {
  params: Promise<{ repoId: string }>;
}) {
  const { repoId } = use(params);

  return (
    <RequireAuth>
      <ChatView repoId={repoId} />
    </RequireAuth>
  );
}
