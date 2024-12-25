package ma.ensa.www.assistdoc;

import androidx.fragment.app.Fragment;


public class Fragment_Chat extends Fragment {
/*
    private FragmentChatBinding binding;
    private ChatAppViewModel viewModel;
    private MessageAdapter adapter;
    private MaterialToolbar toolbar;

    // Initialisation de la vue
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialisation des éléments de la vue
        toolbar = view.findViewById(R.id.toolBarChat);
        ImageView circleImageView = toolbar.findViewById(R.id.chatImageViewUser);
        TextView textViewName = toolbar.findViewById(R.id.chatUserName);
        TextView textViewStatus = view.findViewById(R.id.chatUserStatus);
        ImageView chatBackBtn = toolbar.findViewById(R.id.chatBackBtn);

        // Initialisation du ViewModel
        viewModel = new ViewModelProvider(this).get(ChatAppViewModel.class);

        // Récupération des données utilisateur depuis les arguments du fragment
        Users user = getArguments() != null ? (Users) getArguments().getSerializable("user") : null;

        if (user != null) {
            Glide.with(view.getContext())
                    .load(user.getImageUrl())
                    .placeholder(R.drawable.person)
                    .dontAnimate()
                    .into(circleImageView);

            textViewName.setText(user.getUsername());
            textViewStatus.setText(user.getStatus());
        }

        // Navigation vers le fragment précédent
        chatBackBtn.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });

        // Envoi du message via le ViewModel
        binding.sendBtn.setOnClickListener(v -> {
            if (user != null) {
                viewModel.sendMessage(Utils.getUidLoggedIn(), user.getUserid(), user.getUsername(), user.getImageUrl());
                binding.editTextMessage.setText("");
            }
        });

        // Observer les messages
        if (user != null) {
            viewModel.getMessages(user.getUserid()).observe(getViewLifecycleOwner(), new Observer<java.util.List<Messages>>() {
                @Override
                public void onChanged(java.util.List<Messages> messages) {
                    initRecyclerView(messages);
                }
            });
        }
    }

    // Initialisation de la RecyclerView
    private void initRecyclerView(java.util.List<Messages> list) {
        adapter = new MessageAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.messagesRecyclerView.setLayoutManager(layoutManager);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true); // Afficher le dernier message en bas

        adapter.setList(list);
        adapter.notifyDataSetChanged();
        binding.messagesRecyclerView.setAdapter(adapter);
    }

 */
}
